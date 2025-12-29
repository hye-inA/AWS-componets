package com.demo.dynamodb.dto;

public record CustomerResponse (
        String customerId,
        String name,
        String email,
        Long createAt
) {}
