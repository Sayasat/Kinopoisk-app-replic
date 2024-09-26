package com.sayasat.exp2.kinopoisk_check.dto.directors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirectorNameDTO {

    @NotEmpty(message = "Full Name shouldn't be empty")
    private String fullName;
}
