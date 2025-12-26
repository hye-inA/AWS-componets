package com.example.s3_ex02.service;

import com.example.s3_ex02.dto.S3FileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.List;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // S3 파일 목록 조회
    public List<S3FileDto> listFile() {

    }

    // S3 객체 S3FileDto로 변환

}
