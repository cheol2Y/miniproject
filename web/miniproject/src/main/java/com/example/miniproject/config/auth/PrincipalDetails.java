package com.example.miniproject.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.miniproject.model.entity.UsersEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PrincipalDetails implements UserDetails {

    private UsersEntity usersEntity;

    public PrincipalDetails(UsersEntity usersEntity) {
        this.usersEntity = usersEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        log.info("[PrincipalDetails][getAuthorities] start");
        Collection<GrantedAuthority> collect = new ArrayList<>();
        // collect.add(new SimpleGrantedAuthority(user.getRole()));
        collect.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {
                return usersEntity.getRole();
            }

        });

        return collect;
    }

    @Override
    public String getPassword() {
        log.info("[PrincipalDetails][getPassword] start");
        return usersEntity.getPassword();
    }

    @Override
    public String getUsername() {
        log.info("[PrincipalDetails][getUsername] start");
        return usersEntity.getUsername();
    }

    public String getEmail() {
        return usersEntity.getEmail(); // UsersEntity 클래스에 getEmail() 메서드가 정의되어 있어야 합니다.
    }

    @Override
    public boolean isAccountNonExpired() {
        log.info("[PrincipalDetails][isAccountNonExpired] start");
        // 계정 만료 유무 확인
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        log.info("[PrincipalDetails][PrincipalDetails] start");
        // 계정 잠긴 유무 확인
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        log.info("[PrincipalDetails][isCredentialsNonExpired] start");
        // 계정 비번 오래 사용했는지 유무 확인
        return true;
    }

    @Override
    public boolean isEnabled() {
        log.info("[PrincipalDetails][isEnabled] start");
        // 활성화된 계정인지 유무 확인
        return true;
    }

}