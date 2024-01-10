package com.example.miniproject.config.handler;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException {
        log.info("[CustomAuthenticationEntryPoint] Unauthorized access detected");
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write("<script>alert('로그인이 필요합니다.'); location.href='/loginForm';</script>");
    }
}
