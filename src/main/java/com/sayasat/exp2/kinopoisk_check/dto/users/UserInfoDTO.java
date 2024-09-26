package com.sayasat.exp2.kinopoisk_check.dto.users;

import com.sayasat.exp2.kinopoisk_check.models.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {

    @NotEmpty(message = "Username should consist smth")
    private String username;

    @NotEmpty(message = "Email should exist")
    @Email
    private String email;

    private String role;

    private List<Comment> reviews;
}
