package com.ses.messaging.dto;

import java.util.List;

public record CreateOrderRequest(
        List<Item> items
) {}
