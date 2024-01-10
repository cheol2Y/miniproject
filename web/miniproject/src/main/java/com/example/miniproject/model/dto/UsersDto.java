package com.example.miniproject.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class UsersDto {
    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 8, max = 16, message = "비밀번호는 최소 8자리입니다.")
    private String password;

    @NotNull
    @Email
    private String email;
    private String role;
}
