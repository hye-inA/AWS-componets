package com.ses.messaging.repository;

import com.ses.messaging.entity.Member;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final DynamoDbEnhancedClient enhancedClient;
    private DynamoDbTable<Member> memberTable;

    @Value("${aws.dynamodb.table-name}")
    private String tableName;

    @PostConstruct
    public void init() {
        memberTable = enhancedClient.table(tableName, TableSchema.fromBean(Member.class));
    }

    // 회원 저장
    public void save(Member member) {
        memberTable.putItem(member);
    }

    // 전체 회원 수 조회
    public long count() {
        return memberTable.scan().items().stream().count();
    }
}
