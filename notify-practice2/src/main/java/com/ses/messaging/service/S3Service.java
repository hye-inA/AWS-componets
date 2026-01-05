package com.ses.messaging.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.s3.template-key}")
    private String templateKey;

    // S3에서 welcome.html 템플릿을 가져오기
    public String getWelcomeTemplate() {
        try {
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(templateKey)
                    .build();

            ResponseInputStream<GetObjectResponse> response = s3Client.getObject(request);

            // InputStream → String 변환
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(response, StandardCharsets.UTF_8)
            );
            String template = reader.lines().collect(Collectors.joining("\n"));
            reader.close();

            return template;

        } catch (Exception e) {
            System.err.println("S3 템플릿 로딩 실패: " + e.getMessage());
            // 실패 시 기본 템플릿 반환
            return "<html><body><h1>가입을 환영합니다, {name}님!</h1></body></html>";
        }
    }
}
