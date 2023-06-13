package com.recipeone.security.handler;

import com.recipeone.entity.MemberLoginlog;
import com.recipeone.entity.Memberpagelog;
import com.recipeone.repository.MemberLogRepository;
import com.recipeone.repository.MemberPageRepository;
import com.recipeone.security.dto.MemberSecurityDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
                // 페이지 퇴장 로그 출력
//                System.out.println("페이지 퇴장 - 사용자: " + request.getRemoteUser() + ", 페이지: " + previousPage);


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
                MemberSecurityDTO memberSecurityDTO = (MemberSecurityDTO) authentication.getPrincipal();

                Memberpagelog memberpagelog = Memberpagelog.builder()
                        .mid(username)
                        .page(previousPage)
                        .duration(String.valueOf(formattedDuration))
                        .useryear(memberSecurityDTO.getUseryear())
                        .userlev(memberSecurityDTO.getUserlev())
                        .usergender(memberSecurityDTO.getUsergender())
                        .build();
                memberPageRepository.save(memberpagelog);
            }

            // 페이지 입장 로그 출력
//            System.out.println("페이지 입장 - 사용자: " + request.getRemoteUser() + ", 페이지: " + currentPage);

            // 로그 또는 데이터베이스에 접근 기록 저장
            // ...

            // 접근 시간 업데이트
            session.setAttribute("accessTime", LocalDateTime.now());
        }
    }}
