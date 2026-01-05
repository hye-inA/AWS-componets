package com.ses.messaging.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.pinpointsmsvoicev2.PinpointSmsVoiceV2Client;
import software.amazon.awssdk.services.pinpointsmsvoicev2.model.MessageType;
import software.amazon.awssdk.services.pinpointsmsvoicev2.model.SendTextMessageRequest;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.Body;
import software.amazon.awssdk.services.ses.model.Content;
import software.amazon.awssdk.services.ses.model.Message;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SesClient sesClient;
    private final PinpointSmsVoiceV2Client smsClient;

    @Value("${aws.ses.sender-email}")
    private String senderEmail;

    @Value("${aws.sms.admin-phone}")
    private String adminPhone;

    /**
     * HTML 이메일 발송
     */
    public void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            SendEmailRequest request = SendEmailRequest.builder()
                    .source(senderEmail)
                    .destination(d -> d.toAddresses(to))
                    .message(Message.builder()
                            .subject(Content.builder().data(subject).build())
                            .body(Body.builder()
                                    .html(Content.builder().data(htmlBody).build())  // HTML 모드
                                    .build())
                            .build())
                    .build();

            sesClient.sendEmail(request);
            System.out.println("이메일 발송 성공: " + to);

        } catch (Exception e) {
            System.err.println("이메일 발송 실패: " + e.getMessage());
        }
    }

    /**
     * 관리자에게 SMS 발송
     */
    public void sendAdminSms(String message) {
        try {
            SendTextMessageRequest request = SendTextMessageRequest.builder()
                    .destinationPhoneNumber(adminPhone)
                    .messageBody(message)
                    .messageType(MessageType.TRANSACTIONAL)
                    .build();

            smsClient.sendTextMessage(request);
            System.out.println("관리자 SMS 발송 성공: " + adminPhone);

        } catch (Exception e) {
            System.err.println("SMS 발송 실패: " + e.getMessage());
        }
    }
}
