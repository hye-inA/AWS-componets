package com.ses.messaging.service;

import com.ses.messaging.dto.Item;
import com.ses.messaging.entity.ShopData;
import com.ses.messaging.repository.ShopDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {


    private final ShopDataRepository repository;

    private static final String ITEM_PREFIX = "ITEM#";

    public String createOrder(List<Item> items) {
        // 주문 ID 생성 (공유 PK)
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uuid = UUID.randomUUID().toString().substring(0, 4);  // 첫 4글자만 사용
        String orderId = "ORD_" + timestamp + "_" + uuid;

        // 주문 총액
        Long totalAmount = items.stream()
                .mapToLong(item -> item.price())  // .mapToLong(Item::price)
                .sum();

        // 주문 정보 + 주문 상품 합쳐서 Repository로 전달
        List<ShopData> shopDataList = new ArrayList<>();

        // 주문 정보 저장
        ShopData orderInfo = ShopData.builder()
                .pk(orderId)
                .sk("INFO")
                .type("ORDER")
                .info("ORDER_CREATED")
                .amount(totalAmount)
                .build();
        shopDataList.add(orderInfo);

        // 주문 상품 저장
        for (int i = 0, size = items.size(); i < size; i++) {
            Item item = items.get(i);
            ShopData itemEntity = ShopData.builder()
                    .pk(orderId)
                    .sk(ITEM_PREFIX + String.format("%03d", i + 1))
                    .type("ITEM")
                    .info(item.name())
                    .amount(item.price())
                    .build();
            shopDataList.add(itemEntity);
        }

        // 트랜잭션을 위해서 한 번에 전달
        repository.save(shopDataList);

        // 주문 ID 반환
        return orderId;
    }
}
