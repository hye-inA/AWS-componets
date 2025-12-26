package com.example.demo;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

public class DemoApplication {

	public static void main(String[] args) {

		// try-catch-resource: close 필요없는 try-catch

		// S3Client 생성
		try (S3Client client = S3Client.builder()
				.region(Region.AP_NORTHEAST_2) // 서울
				.build()) {

			// S3 버킷 목록 조회
			client.listBuckets();

			System.out.println("연결 성공! 자격 증명이 올바릅니다");

		} catch (Exception e) {
			System.out.println("연결 실패! 자격 증명을 확인하세요.");
			e.printStackTrace();
		}

	}

}
