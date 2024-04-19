package ru.rtstudy.educplatform.minioservice.service.impl;

import io.minio.*;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.MinioException;
import io.minio.errors.XmlParserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.rtstudy.educplatform.minioservice.dto.UploadResponse;
import ru.rtstudy.educplatform.minioservice.service.S3Service;
import ru.rtstudy.educplatform.minioservice.util.InputStreamCollector;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static ru.rtstudy.educplatform.minioservice.util.Util.createUUID;
import static ru.rtstudy.educplatform.minioservice.util.Util.getMediaType;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3MinioServiceImpl implements S3Service {

    private final MinioAsyncClient minioClient;
    @Value("${spring.minio.bucket}")
    private String bucketName;

    public Mono<UploadResponse> uploadFile(Mono<FilePart> file) {
        return file
                .subscribeOn(Schedulers.boundedElastic())
                .map(multipartFile -> {
                    log.info("Uploading file name: {}", multipartFile.filename());
                    File temp = new File(createUUID(multipartFile));
                    log.info("Change name to: {}", temp.getName());
                    try {
                        multipartFile.transferTo(temp).block();
                        UploadObjectArgs uploadObjectArgs = getUploadObjectArgs(multipartFile, temp);
                        log.debug("File will upload to: {} bucket", uploadObjectArgs.bucket());
                        ObjectWriteResponse response = minioClient.uploadObject(uploadObjectArgs).get();
                        temp.delete();
                        return UploadResponse.builder()
                                .objectName(response.object())
                                .build();
                    } catch (Exception e) {
                        log.error("Uploading was interrupt", new MinioException("File wasn't upload."));
                        throw new RuntimeException(e);
                    }
                }).log();
    }


    public Mono<ByteArrayResource> download(String fileName) {
        return Mono.fromCallable(() -> {
                    log.info("Downloading file: {}", fileName);
                    GetObjectArgs objectArgs = getObjectArgs(fileName);
                    InputStream response = minioClient
                            .getObject(objectArgs)
                            .get();
                    return new ByteArrayResource(response.readAllBytes());
                })
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<UploadResponse> putObject(FilePart file) {
        log.info("Uploading file: {}", file.filename());
        return file.content()
                .subscribeOn(Schedulers.boundedElastic())
                .reduce(new InputStreamCollector(),
                        (collector, dataBuffer) -> collector.collectInputStream(dataBuffer.asInputStream()))
                .map(inputStreamCollector -> {
                    try {
                        PutObjectArgs args = getPutObjectArgs(file, inputStreamCollector);
                        log.debug("File will upload to: {} bucket.", args.bucket());
                        ObjectWriteResponse response = minioClient.putObject(args).get();
                        return UploadResponse.builder()
                                .objectName(response.object())
                                .build();
                    } catch (Exception e) {
                        log.error("File wasn't upload: {}", file.filename(), new MinioException("File wasn't upload."));
                        throw new RuntimeException(e);
                    }
                }).log();
    }

    @Override
    public boolean deleteFile(String fileName) {
        log.info("Delete file: {}", fileName);
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build());
            return true;
        } catch (InsufficientDataException | InternalException | InvalidKeyException | IOException |
                 NoSuchAlgorithmException | XmlParserException e) {
            log.error("File was not deleted: {}", fileName, new MinioException("File wasn't delete"));
            throw new RuntimeException(e);
        }
    }

    private GetObjectArgs getObjectArgs(String fileName) {
        return GetObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .build();
    }

    private UploadObjectArgs getUploadObjectArgs(FilePart multipartFile, File temp) throws IOException {
        return UploadObjectArgs.builder()
                .bucket(bucketName)
                .object(temp.getName())
                .filename(temp.getName())
                .contentType(getMediaType(multipartFile.headers()))
                .build();
    }

    private PutObjectArgs getPutObjectArgs(FilePart file, InputStreamCollector inputStreamCollector) throws IOException {
        PutObjectArgs args = PutObjectArgs.builder()
                .object(createUUID(file))
                .contentType(getMediaType(file.headers()))
                .bucket(bucketName)
                .stream(inputStreamCollector.getStream(), inputStreamCollector.getStream().available(), -1)
                .build();
        return args;
    }
}