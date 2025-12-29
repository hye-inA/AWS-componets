package com.demo.dynamodb.dto;

public record CustomerUpdateRequeset (
        String name,
        String email
) {}
