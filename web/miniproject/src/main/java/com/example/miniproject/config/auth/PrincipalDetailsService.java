package com.example.miniproject.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.miniproject.database.dao.UsersDao;
import com.example.miniproject.model.entity.UsersEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UsersDao usersDao;

    // 시큐리티 session -> Authentication -> UserDetails
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsersEntity userEntity = usersDao.findByUserId(username);
        log.info("[PrincipalDetailsService][loadUserByUsername] start");

        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        log.info(userEntity.toString());
        return new PrincipalDetails(userEntity);
    }
}