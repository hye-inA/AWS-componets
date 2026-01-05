package com.ses.messaging.repository;

import com.ses.messaging.entity.ShopData;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
@RequiredArgsConstructor
public class ShopDataRepository {

    private final DynamoDbEnhancedClient enhancedClient;
    private DynamoDbTable<ShopData> shopTable;

    @PostConstruct
    public void init() {
        shopTable = enhancedClient.table("ShopData-mkt", TableSchema.fromBean(ShopData.class));
    }

    public void save(ShopData shopData) {
        shopTable.putItem(shopData);
    }
}
