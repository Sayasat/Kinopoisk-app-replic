package com.sayasat.exp2.kinopoisk_check.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@Table(name = "Genre")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Genre {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "genre")
    @NotEmpty(message = "Genre should be")
    private String genre;

    @ManyToMany(mappedBy = "genres")
    private List<Movie> movies;
}
