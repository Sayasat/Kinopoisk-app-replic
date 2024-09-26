package com.sayasat.exp2.kinopoisk_check.repositories;

import com.sayasat.exp2.kinopoisk_check.models.Movie;
import com.sayasat.exp2.kinopoisk_check.models.MovieRating;
import com.sayasat.exp2.kinopoisk_check.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRatingRepository extends JpaRepository<MovieRating, Integer> {
    MovieRating findByMovieAndUser(Movie movie, User user);
}
