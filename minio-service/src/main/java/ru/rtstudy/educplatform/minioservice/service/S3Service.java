package ru.rtstudy.educplatform.minioservice.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;
import ru.rtstudy.educplatform.minioservice.dto.UploadResponse;

public interface S3Service {

    Mono<UploadResponse> uploadFile(Mono<FilePart> filePart);

    Mono<ByteArrayResource> download(String fileName);

    Mono<UploadResponse> putObject(FilePart file);

    boolean deleteFile(String fileName);
}
