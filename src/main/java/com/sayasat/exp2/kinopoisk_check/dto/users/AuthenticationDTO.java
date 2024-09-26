package com.sayasat.exp2.kinopoisk_check.dto.users;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationDTO {

    @NotEmpty(message = "Name shouldn't be empty")
    @Size(min = 2, max = 100, message = "Username should be between 2 and 100 characters")
    private String username;

    private String password;
}
