package com.example.miniproject.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.miniproject.model.entity.UsersEntity;

public interface UsersRepository extends JpaRepository<UsersEntity, String> {

}
