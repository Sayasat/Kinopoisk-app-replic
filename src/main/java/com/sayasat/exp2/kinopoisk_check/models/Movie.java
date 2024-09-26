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
import java.util.Set;

@Entity
@Table(name = "Movie")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    @NotEmpty(message = "Title shouldn't be empty")
    private String title;

    @Column(name = "description")
    @NotEmpty(message = "Something must be here")
    private String description;

    @Column(name = "release_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @Column(name = "duration")
    @NotNull
    private Integer duration;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "number_of_votes")
    private int numberOfVotes;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<MovieRating> ratings;

    @ManyToOne
    @JoinColumn(name = "director_id")
    private Director director;

    @ManyToMany
    @JoinTable(
            name = "Movie_Genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres;

    @OneToMany(mappedBy = "movie")
    private List<Comment> comments;

    @ManyToMany
    @JoinTable(
            name = "Movie_Actor",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    private List<Actor> actors;

}



//}
