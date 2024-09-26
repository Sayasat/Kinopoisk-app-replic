package com.sayasat.exp2.kinopoisk_check.dto.actors;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sayasat.exp2.kinopoisk_check.dto.movies.MovieDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActorInfoDTO {

    @NotEmpty(message = "Full Name shouldn't be empty")
    private String fullName;

    @NotEmpty(message = "Biography shouldn't be empty")
    private String biography;

    @NotNull(message = "Birth date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    private List<MovieDTO> movies;
}
