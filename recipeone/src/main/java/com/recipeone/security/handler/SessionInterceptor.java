package com.recipeone.security.handler;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.Duration;
import java.time.LocalDateTime;

public class SessionInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HttpSession session = request.getSession(false);

        if (session != null) {
            // 이전 페이지와 현재 페이지 비교
            String previousPage = (String) session.getAttribute("currentPage");
            String currentPage = request.getRequestURI();
            session.setAttribute("currentPage", currentPage);

            if (previousPage != null && !currentPage.equals(previousPage)) {
                // 페이지 퇴장 로그 출력
//                System.out.println("페이지 퇴장 - 사용자: " + request.getRemoteUser() + ", 페이지: " + previousPage);


                // 접근 시간 업데이트
                LocalDateTime accessTime = (LocalDateTime) session.getAttribute("accessTime");
                LocalDateTime currentTime = LocalDateTime.now();
                Duration duration = Duration.between(accessTime, currentTime);
//                System.out.println("Duration: " + duration);
                System.out.println("사용자: " + request.getRemoteUser() + ", 머무른 페이지: " + previousPage +"머무른 시간: " + duration);

            }

            // 페이지 입장 로그 출력
//            System.out.println("페이지 입장 - 사용자: " + request.getRemoteUser() + ", 페이지: " + currentPage);

            // 로그 또는 데이터베이스에 접근 기록 저장
            // ...

            // 접근 시간 업데이트
            session.setAttribute("accessTime", LocalDateTime.now());
        }
    }}
