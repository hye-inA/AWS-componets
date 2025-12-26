package com.example.s3_ex01.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;
@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFile(MultipartFile file) throws IOException {

        String key = "uploads/"
                + UUID.randomUUID()
                + "_"
                + file.getOriginalFilename(); // 원본파일명


        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(file.getContentType())
                .contentLength(file.getSize())
                .build();

        // S3에 파일 업로드
        s3Client.putObject(
                objectRequest,
                RequestBody.fromInputStream(file.getInputStream(), file.getSize())
        );

        // 업로드된 파일의 URL 생성
        String url = String.format("https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/%s", key);

        return url;
    }
}
