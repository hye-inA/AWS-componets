package com.ses.messaging.dto;

public record CreateOrderResponse(
        String orderId,
        String message
) {}
