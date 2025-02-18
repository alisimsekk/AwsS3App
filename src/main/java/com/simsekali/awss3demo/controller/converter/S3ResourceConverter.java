package com.simsekali.awss3demo.controller.converter;

import com.simsekali.awss3demo.controller.dto.S3ResourceDTO;
import io.awspring.cloud.s3.S3Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3ResourceConverter {

    public S3ResourceDTO convertToDTO(S3Resource s3Resource) {
        return S3ResourceDTO.builder()
                .key(s3Resource.getLocation().getObject())
                .build();
    }
}
