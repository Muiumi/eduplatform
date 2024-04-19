package ru.rtstudy.educplatform.minioservice.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioAsyncClient;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.XmlParserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

@Configuration
@Slf4j
public class MinioConfig {
    @Value("${spring.minio.url}")
    private String url;

    @Value("${spring.minio.access-key}")
    private String userName;

    @Value("${spring.minio.secret-key}")
    private String password;

    @Value("${spring.minio.bucket}")
    private String bucketName;

    @Bean
    public MinioAsyncClient minioClient() {;
        MinioAsyncClient asyncClient = MinioAsyncClient.builder()
                .endpoint(url)
                .credentials(userName, password)
                .build();

        try {
            if (!asyncClient.bucketExists(BucketExistsArgs.builder()
                            .bucket(bucketName)
                            .build())
                    .get()) {
                asyncClient.makeBucket(
                        MakeBucketArgs
                                .builder()
                                .bucket(bucketName)
                                .build()
                );
            }
            log.info("Minio client was created: {}, bucket: {}", userName, bucketName);
            return asyncClient;
        } catch (InsufficientDataException | InternalException | InvalidKeyException | IOException |
                 NoSuchAlgorithmException | XmlParserException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
