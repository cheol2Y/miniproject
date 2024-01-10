package com.example.miniproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.miniproject.config.handler.CustomAuthenticationEntryPoint;
import com.example.miniproject.config.handler.CustomAuthenticationFailureHandler;
import com.example.miniproject.config.handler.CustomAuthenticationSuccessHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 됩니다.
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

    @Bean // 해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다.
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    // Configuring HttpSecurity
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("[SecurityConfig][filterChain] start");
        http.csrf(AbstractHttpConfigurer::disable);
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/charts/**").authenticated() // 인증이되면 접근 가능
                        .requestMatchers("/manager/**").hasAnyAuthority("ADMIN", "MANAGER") // 인증&인가가 되면 접근 가능
                        .requestMatchers("/admin/**").hasAnyAuthority("ADMIN") // 인증&인가가 되면 접근 가능
                        .anyRequest().permitAll() // 누구나 접근 가능
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/loginForm")
                        .loginProcessingUrl("/login") // login 주소가 호출이 되면, PrincipalDetailsService.loadUserByUsername()
                        .successHandler(new CustomAuthenticationSuccessHandler()) // 성공 핸들러 추가
                        .failureHandler(new CustomAuthenticationFailureHandler()) // 여기에 핸들러 추가
                        .permitAll())
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint()));

        return http.build();
    }

}