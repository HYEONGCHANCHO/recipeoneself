package com.recipeone.security.handler;

import com.recipeone.entity.Memberpagelog;
import com.recipeone.repository.MemberPageRepository;
import com.recipeone.security.dto.MemberSecurityDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.Duration;
import java.time.LocalDateTime;

public class SessionInterceptor implements HandlerInterceptor {
    private final MemberPageRepository memberPageRepository;

    public SessionInterceptor(MemberPageRepository memberPageRepository) {
        this.memberPageRepository = memberPageRepository;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HttpSession session = request.getSession(false);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (session != null) {
            // 이전 페이지와 현재 페이지 비교
            String previousPage = (String) session.getAttribute("currentPage");
            String currentPage = request.getRequestURI();
            session.setAttribute("currentPage", currentPage);

            if (previousPage != null && !currentPage.equals(previousPage)) {

                // 접근 시간 업데이트
                LocalDateTime accessTime = (LocalDateTime) session.getAttribute("accessTime");
                LocalDateTime currentTime = LocalDateTime.now();
                Duration duration = Duration.between(accessTime, currentTime);
                long seconds = duration.getSeconds();
                long absSeconds = Math.abs(seconds);
                String formattedDuration = String.format("%02d:%02d:%02d", absSeconds / 3600, (absSeconds % 3600) / 60, absSeconds % 60);
                String username = (request.getRemoteUser() != null) ? request.getRemoteUser() : "비로그인 사용자";
                System.out.println("사용자: " + username + ", 머무른 페이지: " + previousPage +"머무른 시간: " + duration);
                System.out.println("사용자: " + username + ", 머무른 페이지: " + previousPage +"머무른 시간: " + formattedDuration);

               if (!username.equals("비로그인 사용자")){
                MemberSecurityDTO memberSecurityDTO = (MemberSecurityDTO) authentication.getPrincipal();

                Memberpagelog memberpagelog = Memberpagelog.builder()
                        .mid(username)
                        .page(previousPage)
                        .duration(String.valueOf(formattedDuration))
                        .useryear(memberSecurityDTO.getUseryear())
                        .userlev(memberSecurityDTO.getUserlev())
                        .usergender(memberSecurityDTO.getUsergender())
                        .build();
                memberPageRepository.save(memberpagelog);}

                if (username.equals("비로그인 사용자")) {

                Memberpagelog memberpagelog = Memberpagelog.builder()
                        .mid(username)
                        .page(previousPage)
                        .duration(String.valueOf(formattedDuration))
                        .useryear("")
                        .userlev(null)
                        .usergender("")
                        .build();
                memberPageRepository.save(memberpagelog);}
            }

            // 접근 시간 업데이트
            session.setAttribute("accessTime", LocalDateTime.now());
        }
    }}
