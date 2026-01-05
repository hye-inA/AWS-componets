package com.ses.messaging.dto;

public record CreateOrderResponse(
        String orderId,
        Long totalAmount,
        String message
) {}
