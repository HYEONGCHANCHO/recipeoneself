package com.recipeone.security.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomLogoutHandler implements LogoutHandler {
    private final PersistentTokenRepository persistentTokenRepository;

    public CustomLogoutHandler(PersistentTokenRepository persistentTokenRepository) {
        this.persistentTokenRepository = persistentTokenRepository;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String series = (String) request.getAttribute("remember-me-series");
        if (series != null) {
            PersistentRememberMeToken token = persistentTokenRepository.getTokenForSeries(series);
            if (token != null) {
                persistentTokenRepository.removeUserTokens(token.getUsername());
            }
        }
        SecurityContextHolder.clearContext();
    }
}