package com.sayasat.exp2.kinopoisk_check.dto.movies;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sayasat.exp2.kinopoisk_check.dto.actors.ActorDTO;
import com.sayasat.exp2.kinopoisk_check.dto.directors.DirectorNameDTO;
import com.sayasat.exp2.kinopoisk_check.dto.genres.GenreDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieInfoDTO {

    @NotEmpty(message = "Title shouldn't be empty")
    private String title;

    @NotEmpty(message = "Something must be here")
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @NotNull
    private Integer duration;

    private Double rating;

    @Valid
    private DirectorNameDTO director;

    @Valid
    private Set<GenreDTO> genres;

    private List<ActorDTO> actors;

}
