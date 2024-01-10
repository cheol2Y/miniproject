package com.example.miniproject.config.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {
        HttpSession session = request.getSession();

        DefaultSavedRequest savedRequest = (DefaultSavedRequest) requestCache.getRequest(request, response);
        log.info("Login Success. savedRequest: " + savedRequest);
        // 세션 정보 로그에 기록
        log.info("Login Success. Session ID: " + session.getId());

        String targetUrl = null;
        if (savedRequest != null) {
            targetUrl = savedRequest.getRedirectUrl();
        } else if (session != null) {
            targetUrl = (String) session.getAttribute("PRE_AUTH_REQUEST_URI");
        }

        if (targetUrl != null) {
            log.info("Redirecting to Url: " + targetUrl);
            response.sendRedirect(targetUrl);
        } else {
            log.info("Redirecting to default Url: /");
            response.sendRedirect("/"); // 기본 페이지로 리디렉트
        }

        session.setAttribute("message", "로그인에 성공했습니다.");
    }
}
