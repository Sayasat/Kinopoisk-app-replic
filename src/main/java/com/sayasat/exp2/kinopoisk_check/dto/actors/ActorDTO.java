package com.sayasat.exp2.kinopoisk_check.dto.actors;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActorDTO {

    @NotEmpty(message = "Full Name shouldn't be empty")
    private String fullName;

    @NotEmpty(message = "Biography shouldn't be empty")
    private String biography;

    @NotNull(message = "Birth date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

}
