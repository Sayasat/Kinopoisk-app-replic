package com.sayasat.exp2.kinopoisk_check.dto.genres;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenreDTO {

    @NotBlank(message = "Genre should be")
    private String genre;
}
