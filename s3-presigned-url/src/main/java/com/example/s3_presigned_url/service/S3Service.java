package com.example.s3_presigned_url.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;

// PubObjectRequest, bucket, key, contentType + PutObjectPresignRequest
// axiox 2번

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Presigner s3Presigner;

    @Value("${cloud.aws.bucket}")
    private String bucketName;

    public String getPreSignedUrl(String originalFilename, String contentType) {

        String key = UUID.randomUUID() + "_" + originalFilename;

        // PubObjectRequest 생성
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(contentType)
                .build();

        // 프리사인 URL 요청을 위한 PutObjectPresignRequest 생성
        // PutObjectPresignRequest + signatureDuration
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5))
                .putObjectRequest(objectRequest)
                .build();

        // Presigned URL 요청
        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);

        // URL 추출
        return presignedRequest.url().toString();

    }

}
