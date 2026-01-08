package com.studying.first_spring_app.service;

import com.studying.first_spring_app.config.MinioProperties;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class FileStorageService {
    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    @SneakyThrows
    public String upload(MultipartFile file, String fileName) {
        InputStream inputStream = file.getInputStream();

        fileName = String.format("%s.%s", fileName, getExtension(file.getOriginalFilename()));
        minioClient.putObject(PutObjectArgs.builder()
                .object(fileName)
                .bucket(minioProperties.getBucketName())
                .contentType(file.getContentType())
                .stream(inputStream, inputStream.available(), -1)
                .build());
        return fileName;
    }

    private String getExtension(String originalFileName) {
        if (originalFileName == null || !originalFileName.contains(".")) {
            return "bin";
        }
        return originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
    }


    @SneakyThrows
    public void removeObject(String objectId) {
        minioClient.removeObject(RemoveObjectArgs.builder().object(objectId).bucket(minioProperties.getBucketName()).build());
    }

    @SneakyThrows
    public InputStream getObject(String fileName) {
        return minioClient.getObject(
                GetObjectArgs.builder().bucket(minioProperties.getBucketName()).object(fileName).build());
    }
}
