package com.sayasat.exp2.kinopoisk_check.controllers;

import com.sayasat.exp2.kinopoisk_check.dto.directors.DirectorDTO;
import com.sayasat.exp2.kinopoisk_check.dto.movies.MovieResponse;
import com.sayasat.exp2.kinopoisk_check.models.Director;
import com.sayasat.exp2.kinopoisk_check.services.DirectorService;
import com.sayasat.exp2.kinopoisk_check.util.validator.DirectorValidator;
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
@RequestMapping("/directors")
public class DirectorController {

    private final DirectorService directorService;
    private final DirectorValidator directorValidator;

    @Autowired
    public DirectorController(DirectorService directorService, DirectorValidator directorValidator) {
        this.directorService = directorService;
        this.directorValidator = directorValidator;
    }

    @GetMapping("/list")
    public MovieResponse getDirectors() {
        return new MovieResponse(directorService.getAllDirectors());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<HttpStatus> addDirector(@RequestBody @Valid DirectorDTO directorDTO,
                                                  BindingResult bindingResult) {
        Director director = directorService.converToDirector(directorDTO);
        directorValidator.validate(director, bindingResult, false);

        if(bindingResult.hasErrors()) {
            returnErrorsToClient(bindingResult);
        }
        directorService.addDirector(directorDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> updateDirector(@RequestBody @Valid DirectorDTO directorDTO,
                                                     BindingResult bindingResult, @PathVariable int id) {
        Director director = directorService.converToDirector(directorDTO);
        director.setId(id);

        // Добавить ID режиссера в валидатор
        directorValidator.validate(director, bindingResult, true);
        if(bindingResult.hasErrors()) {
            returnErrorsToClient(bindingResult);
        }
        directorService.update(directorDTO, id);
        return ResponseEntity.ok(HttpStatus.UPGRADE_REQUIRED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteDirector(@PathVariable int id) {
        directorService.delete(id);
        return ResponseEntity.ok(HttpStatus.UPGRADE_REQUIRED);
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
