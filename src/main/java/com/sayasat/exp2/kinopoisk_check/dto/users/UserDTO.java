package com.sayasat.exp2.kinopoisk_check.dto.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @NotEmpty(message = "Username should consist smth")
    private String username;

    @NotEmpty(message = "Password shouldn't be empty")
    private String password;

    @NotEmpty(message = "Email should exist")
    @Email
    private String email;
}
