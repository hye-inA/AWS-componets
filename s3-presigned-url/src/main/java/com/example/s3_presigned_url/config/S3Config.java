package com.example.s3_presigned_url.config;

import com.example.s3_presigned_url.S3PresignedUrlApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {


    @Value("${cloud.aws.region}")
    private String region;

    // Presigned URL 발급 전용
    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .region(Region.of(region))
                .build();
    }
}
