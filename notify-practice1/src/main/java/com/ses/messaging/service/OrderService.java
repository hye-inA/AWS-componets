package com.ses.messaging.service;

import com.ses.messaging.dto.CreateOrderRequest;
import com.ses.messaging.dto.CreateOrderResponse;
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
    private final EmailService emailService;

    private static final String ITEM_PREFIX = "ITEM#";

    public CreateOrderResponse createOrder(CreateOrderRequest request) {
        // 주문 ID 생성 (공유 PK)
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uuid = UUID.randomUUID().toString().substring(0, 4);  // 첫 4글자만 사용
        String orderId = "ORD_" + timestamp + "_" + uuid;

        // 주문 상품 목록
        List<Item> items = request.items();

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
                .email(request.email())
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
                    .email(request.email())
                    .build();
            shopDataList.add(itemEntity);
        }

        // 트랜잭션을 위해서 한 번에 전달
        repository.save(shopDataList);

        // 이메일 발송 데이터 준비
        String emailSubject = "[주문 완료] 주문이 성공적으로 접수되었습니다.";
        String emailBody = String.format(
                "고객님, 주문해주셔서 감사합니다.\n\n주문번호: %s\n결제금액: %,d원",
                orderId, totalAmount
        );

        // 이메일 발송
        emailService.sendEmail(request.email(), emailSubject, emailBody);

        // 결과 DTO 반환
        return new CreateOrderResponse(orderId, totalAmount, "주문 완료! 이메일을 확인하세요!");
    }
}
