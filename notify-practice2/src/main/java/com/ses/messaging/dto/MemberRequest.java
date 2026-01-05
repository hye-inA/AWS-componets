package com.ses.messaging.dto;

public record MemberRequest(
        String name,
        String email,
        String phone
) {}
