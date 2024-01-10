package com.example.miniproject.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.miniproject.model.dto.UsersDto;
import com.example.miniproject.service.UsersService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class SecurityController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/")
    public String main(RedirectAttributes redirectAttributes, HttpServletRequest request) {
        log.info("[SecurityController][main] start");

        HttpSession session = request.getSession(false);
        if (session != null) {
            String message = (String) session.getAttribute("message");
            if (message != null) {
                redirectAttributes.addFlashAttribute("message", message);
                session.removeAttribute("message");
            }
        }

        return "index";
    }

    @GetMapping("/charts")
    public String select() {
        log.info("[SecurityController][charts] start");
        return "charts";
    }

    @GetMapping("/profile")
    public String profileShow() {
        log.info("[SecurityController][profile] start");
        return "profile";
    }

    @GetMapping("/table")
    public String tableShow() {
        log.info("[SecurityController][table] start");
        return "tables";
    }

    @GetMapping("/user")
    public @ResponseBody String user() {
        log.info("[SecurityController][user] start");
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin() {
        log.info("[SecurityController][admin] start");
        return "admin";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        log.info("[SecurityController][loginForm] start");
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        log.info("[SecurityController][joinForm] start");
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(@Valid @ModelAttribute UsersDto usersDto, BindingResult result,
            RedirectAttributes redirectAttributes, HttpServletRequest request) {
        log.info("[SecurityController][join] start!!");

        if (result.hasErrors()) {
            log.info("[SecurityController][join] error");
            HttpSession session = request.getSession();
            session.setAttribute("errorMessage", "회원가입에 실패했습니다."); // 에러 메시지 설정
            return "redirect:/joinForm";
        }

        log.info(usersDto.toString());

        usersDto.setRole("USER");
        String rawPassword = usersDto.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        usersDto.setPassword(encPassword);

        log.info(usersDto.toString());
        usersService.saveUser(usersDto);

        redirectAttributes.addFlashAttribute("successMessage", "회원가입에 성공했습니다.");
        return "redirect:/loginForm";
    }

    @GetMapping("/no-secured")
    public @ResponseBody String noSecured() {
        return "no-secured";
    }

    @Secured("ADMIN")
    @GetMapping("/secured")
    public @ResponseBody String secured() {
        return "secured";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/secured-roles")
    public @ResponseBody String securedRoles() {
        return "securedRoles";
    }

    @GetMapping("/fetch-message")
    public ResponseEntity<?> fetchMessage(HttpSession session) {
        String message = (String) session.getAttribute("message");
        if (message != null) {
            session.removeAttribute("message"); // 메시지 표시 후 세션에서 제거
            return ResponseEntity.ok().body(Collections.singletonMap("message", message));
        } else {
            return ResponseEntity.noContent().build(); // 메시지가 없을 경우 No Content 응답
        }
    }
}
