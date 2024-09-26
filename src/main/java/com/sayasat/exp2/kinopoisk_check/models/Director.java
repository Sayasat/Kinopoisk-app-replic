package com.sayasat.exp2.kinopoisk_check.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Director")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Director {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "full_name")
    @NotEmpty(message = "Full Name shouldn't be empty")
    private String fullName;

    @Column(name = "biography")
    @NotEmpty(message = "Biography shouldn't be empty")
    private String biography;

    @Column(name = "birth_date")
    @NotNull(message = "Birth date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @OneToMany(mappedBy = "director")
    private List<Movie> movies;

    public Director(String fullName, String biography, LocalDate birthDate) {
        this.fullName = fullName;
        this.biography = biography;
        this.birthDate = birthDate;
    }
}
