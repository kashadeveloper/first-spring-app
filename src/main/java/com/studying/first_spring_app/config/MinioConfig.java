package com.studying.first_spring_app.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MinioConfig {
    private final MinioProperties minio;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minio.getUrl())
                .credentials(minio.getAccessKey(), minio.getSecretKey())
                .build();
    }

    @SneakyThrows
    @Bean
    public CommandLineRunner initMinioBucket(MinioClient minioClient) {
        return args -> {
            String bucketName = minio.getBucketName();
            boolean bucketFound = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucketName).build()
            );

            if (!bucketFound) {
                log.info("Bucket '{}' not found. Creating...", bucketName);
                minioClient.makeBucket(
                        MakeBucketArgs.builder().bucket(bucketName).build()
                );
                log.info("Bucket '{}' created.", bucketName);
            }
        };
    }
}