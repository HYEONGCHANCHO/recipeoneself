package com.recipeone.repository;

import com.recipeone.entity.Member;
import com.recipeone.entity.MemberRole;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
// 3차병합때 수정한 부분

    @Test
    @Commit
    public void insertMembers() {
        String email = "wjstktpghd2@naver.com";
        String password = "156321Qaz!";
        String nickname = "조형찬";

        Member member = Member.builder()
                .mid(email)
                .password(passwordEncoder.encode(password))
                .useremail(email)
                .build();

        member.addRole(MemberRole.USER);
        member.setUsernickname(nickname);

        // ADMIN 권한 부여
        member.addRole(MemberRole.ADMIN);

        memberRepository.save(member);

        Optional<Member> result = memberRepository.getWithRoles(email);

        Member savedMember = result.orElseThrow();

        log.info(savedMember);
        log.info(savedMember.getRoleSet());

        savedMember.getRoleSet().forEach(memberRole -> log.info(memberRole.name()));
    }
}