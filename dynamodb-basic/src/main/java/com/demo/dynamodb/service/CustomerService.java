package com.demo.dynamodb.service;

import com.demo.dynamodb.entity.Customer;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final DynamoDbEnhancedClient enhanceClient;

    // 테이블 매핑 객체
    private DynamoDbTable<Customer> customerTable;

    @PostConstruct
    public void init() {
        customerTable = enhanceClient.table("Customer-hhi", TableSchema.fromBean(Customer.class));
    }

    // 저장하기 - PK 생성 후 반환
    public String saveCustomer(String name, String email) {
        String customerId = UUID.randomUUID().toString();
        Customer customer = new Customer(
                customerId,
                name,
                email,
                System.currentTimeMillis()
        );

        // 저장 : putItem() 메서드
        customerTable.putItem(customer);

        // PK 반환
        return customerId;
    }

    // 조회하기
    public Customer getCustomer(String customerId) {
        // Key 객체 생성
        Key key = Key.builder()
                    .partitionValue(customerId)
                    .build();

        return customerTable.getItem(key);
    }


}
