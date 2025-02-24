package com.simsekali.awss3demo.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class FileUploadedResponse {
    private String fileKey;
    private String fileUrl;
}
