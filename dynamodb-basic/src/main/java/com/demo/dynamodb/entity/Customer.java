package com.demo.dynamodb.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Setter // Setter 필수 (AWS SDK가 데이터를 채워 넣을 때 Setter를 사용)
@AllArgsConstructor
@NoArgsConstructor // NoArgsConstructor 필수 (AWS SDK가 객체를 만들 때 사용)
@DynamoDbBean // DynamoDB 테이블과 매핑되는 엔티티 (@Entity 역할)
public class Customer {

    private String customerId;
    private String name;
    private String email;
    private Long createAt;

    // Partition Key(PK) 설정 : Getter 메서드에 등록 (@Id 역할)
    @DynamoDbPartitionKey
    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Long getCreateAt() {
        return createAt;
    }
}
