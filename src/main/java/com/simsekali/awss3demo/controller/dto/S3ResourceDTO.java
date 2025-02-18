package com.simsekali.awss3demo.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class S3ResourceDTO {
    private String key;
}
