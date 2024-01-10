package com.example.miniproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.miniproject.database.dao.UsersDao;
import com.example.miniproject.model.dto.UsersDto;
import com.example.miniproject.model.entity.UsersEntity;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UsersService {
    @Autowired
    private UsersDao usersDao;

    public UsersDto findByUserId(String usersId) {
        log.info("[UsersService][findByUserId] start");
        UsersDto usersDto = convertToDto(usersDao.findByUserId(usersId));
        return usersDto;
    }

    public void saveUser(UsersDto userDto) {
        log.info("[UsersService][saveUser] start");
        UsersEntity savedUserEntity = convertToEntity(userDto);
        usersDao.saveUser(savedUserEntity);
    }

    // 엔티티를 DTO로 변환하는 함수
    private UsersDto convertToDto(UsersEntity userEntity) {
        UsersDto userDto = new UsersDto();
        userDto.setUsername(userEntity.getUsername());
        userDto.setPassword(userEntity.getPassword());
        userDto.setEmail(userEntity.getEmail());
        userDto.setRole(userEntity.getRole());

        return userDto;
    }

    // DTO를 엔티티로 변환하는 함수
    private UsersEntity convertToEntity(UsersDto userDto) {
        UsersEntity userEntity = new UsersEntity();
        userEntity.setUsername(userDto.getUsername());
        userEntity.setPassword(userDto.getPassword());
        userEntity.setEmail(userDto.getEmail());
        userEntity.setRole(userDto.getRole());

        return userEntity;
    }
}
