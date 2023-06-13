package com.recipeone.security.handler;

import com.recipeone.entity.Member;
import com.recipeone.entity.MemberLoginlog;
import com.recipeone.repository.MemberLogRepository;
import com.recipeone.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
    private final MemberRepository memberRepository;
    private final MemberLogRepository memberlogRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String username = request.getParameter("username");
        HttpSession session = request.getSession();

        Optional<Member> optionalMember = memberRepository.findById(username);
        Member member = null;
        if (optionalMember.isPresent()) {
            member = optionalMember.get();
            member.setLoginFailCount(0);
            member.setLoginlog(LocalDateTime.now());

            MemberLoginlog memberLoginlog = MemberLoginlog.builder()
                    .mid(username)
                    .loginlog(LocalDateTime.now())
                    .useryear(member.getUseryear())
                    .usergender(member.getUsergender())
                    .userlev(member.getUserlev())
                    .build();
            memberlogRepository.save(memberLoginlog);

            memberRepository.save(member);
        }
        session.setAttribute("loginFailCount", member.getLoginFailCount());

        response.sendRedirect("/");
    }
}