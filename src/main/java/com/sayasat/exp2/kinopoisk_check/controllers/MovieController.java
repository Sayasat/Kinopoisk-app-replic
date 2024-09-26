package com.sayasat.exp2.kinopoisk_check.controllers;

import com.sayasat.exp2.kinopoisk_check.dto.genres.GenreCheckMovie;
import com.sayasat.exp2.kinopoisk_check.dto.SearchResult;
import com.sayasat.exp2.kinopoisk_check.dto.movies.*;
import com.sayasat.exp2.kinopoisk_check.services.GenreService;
import com.sayasat.exp2.kinopoisk_check.services.MovieService;
import com.sayasat.exp2.kinopoisk_check.services.UserContextService;
import com.sayasat.exp2.kinopoisk_check.util.GenreNotFoundException;
import com.sayasat.exp2.kinopoisk_check.util.MovieErrorResponse;
import com.sayasat.exp2.kinopoisk_check.util.MovieException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.sayasat.exp2.kinopoisk_check.util.ErrorsUtil.returnErrorsToClient;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;
    private final GenreService genreService;
    private final UserContextService userContextService;

    @Autowired
    public MovieController(MovieService movieService, GenreService genreService, UserContextService userContextService) {
        this.movieService = movieService;
        this.genreService = genreService;
        this.userContextService = userContextService;
    }

    @GetMapping("/list")
    public MovieResponse getMovies() {
        return new MovieResponse(movieService.getAllMovies());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<HttpStatus> addMovie(@RequestBody @Valid MovieDTO movieDTO,
                                               BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            returnErrorsToClient(bindingResult);
        }
        movieService.save(movieDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> updateMovie(@RequestBody @Valid MovieDTO movieDTO,
                                                  BindingResult bindingResult, @PathVariable int id) {
        if(bindingResult.hasErrors()) {
            returnErrorsToClient(bindingResult);
        }
        movieService.update(movieDTO, id);
        return ResponseEntity.ok(HttpStatus.UPGRADE_REQUIRED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteMovie(@PathVariable int id) {
        movieService.delete(id);
        return ResponseEntity.ok(HttpStatus.UPGRADE_REQUIRED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/addActor")
    public ResponseEntity<HttpStatus> addActor(@RequestBody @Valid MovieActorDTO dto,
                                               BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            returnErrorsToClient(bindingResult);
        }
        movieService.addActor(dto);
        return ResponseEntity.ok(HttpStatus.UPGRADE_REQUIRED);
    }

    @GetMapping("/{id}")
    public MovieInfoDTO getMovie(@PathVariable int id) {
        return movieService.getMovie(id);
    }


    @GetMapping("/search")
    public SearchResult searchMovie(@RequestParam(required = false) String search) {
        List<MovieDTO> movies = movieService.searchMoviesByTitle(search);
        List<GenreCheckMovie> genres = genreService.findGenresByName(search);
        return new SearchResult(movies, genres);
    }

    @PostMapping("{id}/rate")
    public ResponseEntity<HttpStatus> giveRating(@PathVariable int id,
                                                 @RequestBody MovieRatingDTO movieRatingDTO,
                                                 BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            returnErrorsToClient(bindingResult);
        }
        String username = userContextService.getCurrentUsername();
        movieService.rateMovie(id, movieRatingDTO.getRating(), username);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<MovieErrorResponse> handleException(MovieException e) {
        MovieErrorResponse response = new MovieErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<MovieErrorResponse> handleException(GenreNotFoundException e) {
        MovieErrorResponse response = new MovieErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
