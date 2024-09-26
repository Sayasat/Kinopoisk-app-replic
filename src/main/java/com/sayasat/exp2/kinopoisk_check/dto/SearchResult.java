package com.sayasat.exp2.kinopoisk_check.dto;

import com.sayasat.exp2.kinopoisk_check.dto.genres.GenreCheckMovie;
import com.sayasat.exp2.kinopoisk_check.dto.movies.MovieDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResult {

    private List<MovieDTO> movies;
    private List<GenreCheckMovie> genres;
}
