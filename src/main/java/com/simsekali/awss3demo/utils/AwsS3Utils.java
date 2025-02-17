package com.simsekali.awss3demo.utils;

import org.springframework.http.MediaType;

public class AwsS3Utils {

    private AwsS3Utils() {
    }

    public static String determineContentType(String key) {
        if (key.endsWith(".jpg") || key.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG_VALUE;
        } else if (key.endsWith(".png")) {
            return MediaType.IMAGE_PNG_VALUE;
        } else if (key.endsWith(".pdf")) {
            return MediaType.APPLICATION_PDF_VALUE;
        } else if (key.endsWith(".txt")) {
            return MediaType.TEXT_PLAIN_VALUE;
        } else {
            return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
    }
}
