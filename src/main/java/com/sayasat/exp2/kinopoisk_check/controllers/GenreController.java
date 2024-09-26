package com.sayasat.exp2.kinopoisk_check.controllers;

import com.sayasat.exp2.kinopoisk_check.dto.genres.GenreDTO;
import com.sayasat.exp2.kinopoisk_check.dto.movies.MovieResponse;
import com.sayasat.exp2.kinopoisk_check.services.GenreService;
import com.sayasat.exp2.kinopoisk_check.util.validator.GenreValidator;
import com.sayasat.exp2.kinopoisk_check.util.MovieErrorResponse;
import com.sayasat.exp2.kinopoisk_check.util.MovieException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.sayasat.exp2.kinopoisk_check.util.ErrorsUtil.returnErrorsToClient;

@RestController
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;
    private final GenreValidator genreValidator;

    @Autowired
    public GenreController(GenreService genreService, GenreValidator genreValidator) {
        this.genreService = genreService;
        this.genreValidator = genreValidator;
    }

    @GetMapping("/list")
    public MovieResponse getGenres() {
        return new MovieResponse(genreService.getAllGenres());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<HttpStatus> addGenre(@RequestBody @Valid GenreDTO genreDTO,
                                               BindingResult bindingResult) {
        genreValidator.validate(genreService.convertToGenre(genreDTO), bindingResult);

        if(bindingResult.hasErrors()) {
            returnErrorsToClient(bindingResult);
        }
        genreService.addGenre(genreDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> updateGenre(@RequestBody @Valid GenreDTO genreDTO,
                                                  BindingResult bindingResult, @PathVariable int id) {
        genreValidator.validate(genreService.convertToGenre(genreDTO), bindingResult);
        if(bindingResult.hasErrors()) {
            returnErrorsToClient(bindingResult);
        }
        genreService.updateGenre(genreDTO, id);
        return  ResponseEntity.ok(HttpStatus.UPGRADE_REQUIRED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteGenre(@PathVariable int id) {
        genreService.deleteGenre(id);
        return  ResponseEntity.ok(HttpStatus.UPGRADE_REQUIRED);
    }

    @ExceptionHandler
    private ResponseEntity<MovieErrorResponse> handleException(MovieException e) {
        MovieErrorResponse response = new MovieErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
