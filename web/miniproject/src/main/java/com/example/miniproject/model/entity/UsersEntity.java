package com.example.miniproject.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "UsersEntity")
@Table(name = "users")
public class UsersEntity {

    @Id
    private String username;
    private String password;
    @Column(unique = true)
    private String email;
    private String role;
}
