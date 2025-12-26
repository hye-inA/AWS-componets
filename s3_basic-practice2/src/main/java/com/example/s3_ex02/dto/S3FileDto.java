package com.example.s3_ex02.dto;

// recode 클래스
// 모든 필드는 자동으로 private final 처리
// @NoArgsConstructor 미지원
// @AllArgsConstructor 지원
// @Setter 미지원
// @Getter 지원 (get 필요 없음)

public record S3FileDto (
    String key,
    Long size,
    String lastModified

){
    // public S3FileDto(String key, size) {
    // this(key, size, "default-value");
    // }
}
