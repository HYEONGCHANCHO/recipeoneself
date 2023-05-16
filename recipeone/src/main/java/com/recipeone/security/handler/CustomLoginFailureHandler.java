package com.recipeone.security.handler;

import com.recipeone.entity.Member;
import com.recipeone.entity.MemberRole;
import com.recipeone.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor

public class CustomLoginFailureHandler implements AuthenticationFailureHandler {
    private final MemberRepository memberRepository;

    @Override //로그인 5회 실패 시 유저권한 변경
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String username = request.getParameter("username");
        HttpSession session = request.getSession();

        Optional<Member> optionalMember = memberRepository.findById(username);
        if (!optionalMember.isPresent()) {
            response.sendRedirect("/member/login?error=true");
            return;
        }

        Member member = optionalMember.get();
        member.setLoginFailCount(member.getLoginFailCount() + 1);
        int rstloginfailCount=7;
        if (member.getLoginFailCount() >= rstloginfailCount) {
            member.setRoleSet(Collections.singleton(MemberRole.STOP));
        }
        memberRepository.save(member);
        log.info("CustomLoginFailureHandler onAuthenticationFailure ...........");
        log.info(exception.getMessage());
        session.setAttribute("loginFailCount", member.getLoginFailCount());

        response.sendRedirect("/member/login?error=true");
    }
}