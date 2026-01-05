package com.ses.messaging.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.pinpointsmsvoicev2.PinpointSmsVoiceV2Client;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.ses.SesClient;

@Configuration
public class AwsSdkConfig {

    private final Region SEOUL_REGION = Region.AP_NORTHEAST_2;

    // S3 클라이언트
    @Bean
    S3Client s3Client() {
        return S3Client.builder()
                .region(SEOUL_REGION)
                .build();
    }

    // DynamoDB 표준 클라이언트
    @Bean
    DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
                .region(SEOUL_REGION)
                .build();
    }

    // DynamoDB 향상 클라이언트
    @Bean
    DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }

    // 이메일 전송을 위한 SesClient
    @Bean
    SesClient sesClient() {
        return SesClient.builder()
                .region(SEOUL_REGION)
                .build();
    }

    // 문자 전송을 위한 PinpointSmsVoiceV2Client
    @Bean
    PinpointSmsVoiceV2Client smsClient() {
        return PinpointSmsVoiceV2Client.builder()
                .region(SEOUL_REGION)
                .build();
    }
}
