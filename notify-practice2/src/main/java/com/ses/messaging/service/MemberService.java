package com.ses.messaging.service;

import com.ses.messaging.dto.MemberRequest;
import com.ses.messaging.dto.MemberResponse;
import com.ses.messaging.entity.Member;
import com.ses.messaging.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final S3Service s3Service;
    private final NotificationService notificationService;

    /**
     * 멤버십 가입
     */
    public MemberResponse register(MemberRequest request) {
        // 1. 회원 ID 생성
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uuid = UUID.randomUUID().toString().substring(0, 4);
        String memberId = "MEM_" + timestamp + "_" + uuid;

        // 2. 회원 정보 저장 (DynamoDB)
        Member member = Member.builder()
                .memberId(memberId)
                .name(request.name())
                .email(request.email())
                .phone(request.phone())
                .build();
        memberRepository.save(member);

        // 3. S3에서 welcome.html 템플릿 가져오기
        String template = s3Service.getWelcomeTemplate();

        // 4. {name}을 실제 이름으로 치환
        String htmlContent = template.replace("{name}", request.name());

        // 5. 가입 축하 이메일 발송 (SES - HTML)
        notificationService.sendHtmlEmail(
                request.email(),
                "[멤버십] 가입을 환영합니다!",
                htmlContent
        );

        // 6. 관리자에게 SMS 발송
        notificationService.sendAdminSms(request.name());

        // 7. 응답 반환
        return new MemberResponse(memberId, "가입 완료! 이메일을 확인하세요.");
    }
}
