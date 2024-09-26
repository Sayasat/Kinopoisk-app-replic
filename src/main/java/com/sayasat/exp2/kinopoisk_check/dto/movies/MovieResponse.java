package com.sayasat.exp2.kinopoisk_check.dto.movies;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieResponse {

    private List<?> movies;
//    private int totalPages;
//    private long totalElements;
}
