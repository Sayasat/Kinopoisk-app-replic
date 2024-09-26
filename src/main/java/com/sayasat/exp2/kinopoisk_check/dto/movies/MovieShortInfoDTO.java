package com.sayasat.exp2.kinopoisk_check.dto.movies;

import com.sayasat.exp2.kinopoisk_check.dto.directors.DirectorNameDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class  MovieShortInfoDTO {

    @NotEmpty(message = "Title shouldn't be empty")
    private String title;

    @Valid
    private DirectorNameDTO director;

}
