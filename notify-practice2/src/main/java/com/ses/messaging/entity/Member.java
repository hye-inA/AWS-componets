package com.ses.messaging.entity;

import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
public class Member {

    private String memberId;
    private String name;
    private String email;
    private String phone;

    @DynamoDbPartitionKey
    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
