package com.example.miniproject.config.handler;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException {
        HttpSession session = request.getSession();
        // 로그인 실패 시 메세지 작성
        log.error("Login failed for user: " + request.getParameter("username"));
        log.error("Error Message: " + exception.getMessage());

        response.sendRedirect("/loginForm");
        session.setAttribute("message", "로그인에 실패했습니다.");

    }
}
