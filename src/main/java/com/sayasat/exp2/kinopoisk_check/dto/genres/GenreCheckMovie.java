package com.sayasat.exp2.kinopoisk_check.dto.genres;

import com.sayasat.exp2.kinopoisk_check.dto.movies.MovieShortInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenreCheckMovie {

    @NotEmpty(message = "Genre should be")
    private String genre;

    private List<MovieShortInfoDTO> movies;
}
