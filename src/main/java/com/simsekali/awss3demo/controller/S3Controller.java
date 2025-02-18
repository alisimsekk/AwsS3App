package com.simsekali.awss3demo.controller;

import com.simsekali.awss3demo.controller.dto.FileUploadedResponse;
import com.simsekali.awss3demo.controller.dto.S3ResourceDTO;
import com.simsekali.awss3demo.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.simsekali.awss3demo.utils.AwsS3Utils.determineContentType;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
@Slf4j
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping("/upload")
    public ResponseEntity<FileUploadedResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        String key = s3Service.uploadFile(file);
        String fileUrl = s3Service.getFileUrl(key);

        FileUploadedResponse response = FileUploadedResponse.builder()
                .fileKey(key)
                .fileUrl(fileUrl)
                .build();

        log.info("File uploaded. URL : {}", fileUrl);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{key}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String key) {
        byte[] fileData = s3Service.downloadFile(key);
        String contentType = determineContentType(key);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentDispositionFormData("attachment", key);

        return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
    }

    @GetMapping
    public List<S3ResourceDTO> listFiles() {
        return s3Service.listObjects();
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<Void> deleteFile(@PathVariable String key) {
        s3Service.deleteFile(key);
        return ResponseEntity.ok().build();
    }
}
