package com.recipeone.security.handler;

import com.recipeone.entity.Member;
import com.recipeone.entity.MemberLoginlog;
import com.recipeone.repository.MemberLogRepository;
import com.recipeone.repository.MemberRepository;
import com.recipeone.security.dto.MemberSecurityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
public class CustomSocialLoginSuccessHandler implements AuthenticationSuccessHandler {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final MemberLogRepository memberlogRepository;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException{
        MemberSecurityDTO memberSecurityDTO = (MemberSecurityDTO) authentication.getPrincipal();
        String encodedPw = memberSecurityDTO.getPassword();

        MemberLoginlog memberLoginlog = MemberLoginlog.builder()
                .mid(memberSecurityDTO.getMid())
                .loginlog(LocalDateTime.now())
                .build();
        memberlogRepository.save(memberLoginlog);

        //소셜 로그인이고 회원의 패스워드가 1111(초기값)
        if (memberSecurityDTO.isSocial()&&(memberSecurityDTO.getPassword().equals("1111") || passwordEncoder.matches("1111",memberSecurityDTO.getPassword()))){
            response.sendRedirect("/member/socialmodify");
        }else {
            response.sendRedirect("/");
        }
    }
}
