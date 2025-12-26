package com.example.s3.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@RestController
public class S3UploadController {

    private final S3Client s3Client;

    public S3UploadController(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    // [업로드 : 버킷 생성 + 객체 업로드]
    @GetMapping("/upload")
    public String upload() {

        // 버킷 이름 (전 세계 유일)
        String bucket = "hhi-" + UUID.randomUUID().toString();

        // 객체 이름
        String key = "uploads/hello.txt";

        // url 변수 초기화
        String url = "";

        try {
            s3Client.createBucket(CreateBucketRequest.builder()
                    .bucket(bucket)
                    .build());

            // 로컬 파일 업로드
            String filename = "/pc/files/hello.txt";

            // PutObjectRequest(봉투) + ReqeustBody(내용물) 전송
            PutObjectRequest objectReqeust = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(MediaType.TEXT_PLAIN_VALUE)
                    .build();

            // 내용물
            // RequestBody.fromFile(Paths.get(filename));

            // S3에 업로드
            s3Client.putObject(objectReqeust, RequestBody.fromFile(Paths.get(filename)));

            // 반환값 만들기
            url = "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + key;

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        return url;

    }
}