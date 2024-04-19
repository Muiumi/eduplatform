package ru.rtstudy.educplatform.minioservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.rtstudy.educplatform.minioservice.responsebuilder.S3ResponseBuilder;
import ru.rtstudy.educplatform.minioservice.dto.UploadResponse;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "MinIO Controller", description = "MinIO Controller API")
@RequestMapping("/object")
@CrossOrigin
public class S3Controller {

    private final S3ResponseBuilder responseBuilder;

    @Operation(summary = "Загрузка файла в MinIO путём создания его копии и сохранения её в хранилище MinIO")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<UploadResponse> upload(@Parameter(description = "Опциональная строка для передачи токена", in = ParameterIn.HEADER)
                                       @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                       @Parameter(
                                               description = "Файл, который необходимо загрузить в хранилище",
                                               content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE))
                                       @RequestPart(value = "files") Mono<FilePart> files) {
        return responseBuilder.upload(token, files);
    }

    @Operation(summary = "Загрузка файла в MinIO путём прямого преобразования файла в поток байтов")
    @PostMapping(value = "stream", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<UploadResponse> uploadStream(@Parameter(description = "Опциональная строка для передачи токена", in = ParameterIn.HEADER)
                                             @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                             @Parameter(
                                                     description = "Файл, который необходимо загрузить в хранилище",
                                                     content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
                                             @RequestPart(value = "files") FilePart files) {
        return responseBuilder.uploadStream(token, files);
    }

    @Operation(summary = "Загрузка файла из хранилища MinIO по его UUID")
    @GetMapping("{file_name}")
    public ResponseEntity<Mono<ByteArrayResource>> download(@Parameter(description = "Опциональная строка для передачи токена", in = ParameterIn.HEADER)
                                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                            @Parameter(description = "UUID файла")
                                                            @PathVariable(value = "file_name") String fileName) {
        return responseBuilder.download(token, fileName);
    }

    @Operation(summary = "Удаление из хранилища MinIO по его UUID")
    @DeleteMapping("{file_name}")
    public Mono<HttpStatus> deleteFile(@Parameter(description = "Опциональная строка для передачи токена", in = ParameterIn.HEADER)
                                       @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                       @Parameter(description = "UUID файла")
                                       @PathVariable(value = "file_name") String fileName) {
        return responseBuilder.deleteFile(token, fileName);
    }
}
