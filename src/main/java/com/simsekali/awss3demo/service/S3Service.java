package com.simsekali.awss3demo.service;

import com.simsekali.awss3demo.exception.FileDownloadException;
import com.simsekali.awss3demo.exception.FileUploadException;
import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {

    private final S3Template s3Template;

    @Value("${spring.cloud.aws.s3.bucketName}")
    private String bucketName;

    public String uploadFile(MultipartFile file) {
        String key = generateUniqueFileName(file.getOriginalFilename());
        String contentType = file.getContentType();

        ObjectMetadata metadata = ObjectMetadata.builder()
                .contentType(contentType)
                .contentLength(file.getSize())
                .build();

        try {
            s3Template.upload(bucketName, key, file.getInputStream(), metadata);
        } catch (IOException ex) {
            throw new FileUploadException("Failed to upload file");
        }

        log.info("File uploaded successfully. Key {}", key);
        return key;
    }

    public byte[] downloadFile(String key) {
        Resource resource = s3Template.download(bucketName, key);
        try (InputStream inputStream = resource.getInputStream()) {
            return inputStream.readAllBytes();
        } catch (IOException ex) {
            throw new FileDownloadException("Failed to download file");
        }
    }

    public String getFileUrl(String key) {
        URL originalUrl = s3Template.createSignedGetURL(bucketName, key, Duration.ofDays(3L));
        String originalUrlString = originalUrl.toString();

        if (originalUrlString.contains("localhost")) {
            return originalUrlString.replace(
                    "http://" + bucketName + ".localhost:4566/",
                    "http://localhost:4566/" + bucketName + "/"
            );
        }
        return originalUrlString;
    }

    private String generateUniqueFileName(String originalFilename) {
        return UUID.randomUUID() + "_" + originalFilename;
    }
}
