package com.simsekali.awss3demo.service;

import com.simsekali.awss3demo.controller.converter.S3ResourceConverter;
import com.simsekali.awss3demo.controller.dto.S3ResourceDTO;
import com.simsekali.awss3demo.exception.FileDownloadException;
import com.simsekali.awss3demo.exception.FileUploadException;
import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Resource;
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
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {

    private final S3Template s3Template;
    private final S3ResourceConverter s3ResourceConverter;

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

    public List<S3ResourceDTO> listObjects() {
        List<S3Resource> resources = s3Template.listObjects(bucketName, "");
        return resources.stream().map(s3ResourceConverter::convertToDTO).toList();
    }

    public byte[] downloadFile(String key) {
        InputStream inputStream = null;
        try {
            Resource resource = s3Template.download(bucketName, key);
            inputStream = resource.getInputStream();
            return inputStream.readAllBytes();
        } catch (Exception ex) {
            log.error("Error downloading file", ex);
            throw new FileDownloadException(ex.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("Failed to close input stream", e);
                }
            }
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

    public void deleteFile(String key) {
        s3Template.deleteObject(bucketName, key);
        log.info("File deleted successfully. Key {}", key);
    }

    private String generateUniqueFileName(String originalFilename) {
        return UUID.randomUUID() + "_" + originalFilename;
    }
}
