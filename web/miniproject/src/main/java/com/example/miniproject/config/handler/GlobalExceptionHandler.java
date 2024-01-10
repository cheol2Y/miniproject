package com.example.miniproject.config.handler;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    // DataIntegrityViolationException 예외 처리
    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDataIntegrityViolationException(DataIntegrityViolationException e,
            RedirectAttributes redirectAttributes) {
        // 예외 메시지에서 중요한 정보만 추출하여 사용자에게 전달
        String errorMessage = "이미 사용 중인 이메일 주소입니다. 다른 이메일 주소를 사용해 주세요.";
        redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        return "redirect:/joinForm";
    }
}
