package com.ses.messaging.controller;

import com.ses.messaging.dto.MemberRequest;
import com.ses.messaging.dto.MemberResponse;
import com.ses.messaging.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    /**
     * [멤버십 가입]
     * POST /api/members
     * Body {"name": "홍길동", "email": "user@example.com", "phone": "+821012345678"}
     */
    @PostMapping
    public ResponseEntity<MemberResponse> register(@RequestBody MemberRequest request) {
        MemberResponse response = memberService.register(request);
        return ResponseEntity.status(201).body(response);
    }
}
