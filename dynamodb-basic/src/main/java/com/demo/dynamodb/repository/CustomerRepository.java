package com.demo.dynamodb.repository;

import com.demo.dynamodb.entity.Customer;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
@RequiredArgsConstructor
public class CustomerRepository {

    private final DynamoDbEnhancedClient enhanceClient;

    private DynamoDbTable<Customer> customerTable;

    @PostConstruct
    public void init() {
        customerTable = enhanceClient.table("Customer-hhi", TableSchema.fromBean(Customer.class));
    }

    public void save(Customer customer) {
        customerTable.putItem(customer);
    }

    public Customer findById (String customerId) {
        // Key 객체 생성
        Key key = Key.builder()
                .partitionValue(customerId)
                .build();

        return customerTable.getItem(key);
    }

    public void deleteById (String customerId) {
        Key key = Key.builder()
                .partitionValue(customerId)
                .build();

        customerTable.deleteItem(key);

    }
    public Customer update(Customer customer) {
        return customerTable.updateItem(customer);
    }
}
