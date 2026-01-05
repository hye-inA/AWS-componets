package com.ses.messaging.controller;

import com.ses.messaging.dto.CreateOrderRequest;
import com.ses.messaging.dto.CreateOrderResponse;
import com.ses.messaging.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    /**
     * [주문 생성]
     * POST /api/orders
     * Body {"items": [{"name": "PIZZA", "price": 15000}, {"name": "COKE", "price": 5000}]}
     */
    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
        String newOrderId = orderService.createOrder(request.items());
        CreateOrderResponse response = new CreateOrderResponse(newOrderId, "Order Created!");
        return ResponseEntity.status(201).body(response);
    }

}
