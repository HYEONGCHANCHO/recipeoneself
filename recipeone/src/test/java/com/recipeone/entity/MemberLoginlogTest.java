package com.recipeone.entity;

import com.recipeone.repository.MemberLogRepository;
import lombok.ToString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
//@Transactional
@ToString
class MemberLoginlogTest {
    @Autowired
    private MemberLogRepository memberLoginlogRepository;

    @Test
    void testMemberLoginlogSave() {
        String[] mids = {"aa", "aa", "bb", "cc", "dd", "dd", "dd"};
        int[] logs = {1, 2, 3, 4, 5, 6, 7};

        for (int i = 0; i < mids.length; i++) {
            // 새로운 MemberLoginlog 생성
            MemberLoginlog memberLoginlog = MemberLoginlog.builder()
                    .mid(mids[i])
                    .loginlog(LocalDateTime.now())
                    .build();

            // 저장
            memberLoginlogRepository.save(memberLoginlog);

            // 시퀀스로 생성된 ID 확인
            Assertions.assertNotNull(memberLoginlog.getId());
        }

    }
}