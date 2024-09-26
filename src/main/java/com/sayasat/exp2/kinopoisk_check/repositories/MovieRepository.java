package com.sayasat.exp2.kinopoisk_check.repositories;

import com.sayasat.exp2.kinopoisk_check.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

    List<Movie> findByTitleStartingWith(String title);

    List<Movie> findByTitleContainingIgnoreCase(String title);
}
