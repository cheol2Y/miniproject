package com.example.miniproject.database.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.miniproject.database.repository.UsersRepository;
import com.example.miniproject.model.dto.UsersDto;
import com.example.miniproject.model.entity.UsersEntity;

@Service
public class UsersDao {
    @Autowired
    private UsersRepository usersRepository;

    public UsersEntity findByUserId(String username) {
        UsersEntity userEntity = usersRepository.findById(username).orElse(null);
        return userEntity;
    }

    public List<UsersDto> findAllUsers() {
        List<UsersEntity> usersList = usersRepository.findAll();
        return convertToDtoList(usersList);
    }

    public void saveUser(UsersEntity userEntity) {
        usersRepository.save(userEntity);
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

    // 엔티티 리스트를 DTO 리스트로 변환하는 함수
    private List<UsersDto> convertToDtoList(List<UsersEntity> usersList) {
        List<UsersDto> usersDtoList = new ArrayList<>();
        for (UsersEntity userEntity : usersList) {
            usersDtoList.add(convertToDto(userEntity));
        }
        return usersDtoList;
    }

}