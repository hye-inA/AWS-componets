package com.demo.dynamodb.dto;

public record CustomerCreateRequest(
        String name,
        String email
) {}
