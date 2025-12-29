package com.example.s3_presigned_url.controller;

import com.example.s3_presigned_url.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    @GetMapping("/presigned-url")
    public String getUrl(
            @RequestParam("key") String key,
            @RequestParam("contentType") String contentType) {
        return s3Service.getPreSignedUrl(key, contentType);
    }
}
