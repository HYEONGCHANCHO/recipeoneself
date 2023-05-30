package com.recipeone.config;

import com.recipeone.repository.MemberRepository;
import com.recipeone.security.CustomUserDetailService;
import com.recipeone.security.handler.*;
//import com.recipeone.security.handler.CustomAuthenticationFailureHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.persistence.Access;
import javax.sql.DataSource;

@Log4j2
@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)

public class SecurityConfig  {

    private final DataSource dataSource;
    private final CustomUserDetailService userDetailService;
    private final MemberRepository memberRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("-------------conofigure--------------");

        http.formLogin().loginPage("/member/login")
                .failureHandler(authenticationFailureHandler())
                .successHandler(customLoginSuccessHandler()); //일반 로그인 성공에 대한 핸들러 등록


        http.csrf().disable();

        http.rememberMe()
                .key("12345678") //application properties에서 나중에 바꿔줄것
                .tokenRepository(persistentTokenRepository())
                .userDetailsService(userDetailService)
                .tokenValiditySeconds(60 * 60 * 24 * 30);

        http.logout()
                .logoutUrl("/logout") //로그아웃 URL
                .logoutSuccessUrl("/") //로그아웃 성공시 이동할 URL
                .invalidateHttpSession(true) //HttpSession 무효화 여부
                .deleteCookies("JSESSIONID", "remember-me") //삭제할 쿠키 이름
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) //로그아웃 요청 매칭 규칙
                .addLogoutHandler(new CustomLogoutHandler(persistentTokenRepository())); //로그아웃 핸들러 추가


        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());

        http.oauth2Login().loginPage("/member/login").successHandler(authenticationSuccessHandler());

        return http.build();
    }

    @Bean //정적 메서드 시큐리티 제외
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean //로그인 지속
    public PersistentTokenRepository persistentTokenRepository() { //rememberme db에 저장
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        return repo;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean //권한 제한 될 때
    public AccessDeniedHandler accessDeniedHandler() {
        return new Custom403Handler();
    }

    @Bean //소셜 로그인 성공
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomSocialLoginSuccessHandler(passwordEncoder());
    }

    @Bean//일반 로그인 실패
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomLoginFailureHandler(memberRepository);
    }
    @Bean //일반 로그인 성공
    public AuthenticationSuccessHandler customLoginSuccessHandler() {
        return new CustomLoginSuccessHandler(memberRepository);
    }



}
