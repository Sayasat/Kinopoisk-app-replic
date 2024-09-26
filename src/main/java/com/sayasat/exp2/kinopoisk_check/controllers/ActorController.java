package com.sayasat.exp2.kinopoisk_check.controllers;

import com.sayasat.exp2.kinopoisk_check.dto.actors.ActorDTO;
import com.sayasat.exp2.kinopoisk_check.dto.actors.ActorInfoDTO;
import com.sayasat.exp2.kinopoisk_check.dto.movies.MovieResponse;
import com.sayasat.exp2.kinopoisk_check.models.Actor;
import com.sayasat.exp2.kinopoisk_check.services.ActorService;
import com.sayasat.exp2.kinopoisk_check.util.validator.ActorValidator;
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
@RequestMapping("/actors")
public class ActorController {

    private final ActorService actorService;
    private final ActorValidator actorValidator;

    @Autowired
    public ActorController(ActorService actorService, ActorValidator actorValidator) {
        this.actorService = actorService;
        this.actorValidator = actorValidator;
    }

    @GetMapping("/list")
    public MovieResponse getActors() {
        return new MovieResponse(actorService.getAllActors());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<HttpStatus> addActor(@RequestBody @Valid ActorDTO actorDTO,
                                               BindingResult bindingResult) {
        Actor actor = actorService.convertToActor(actorDTO);
        actorValidator.validate(actor, bindingResult, false);

        if(bindingResult.hasErrors()) {
            returnErrorsToClient(bindingResult);
        }
        actorService.addActor(actorDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> updateActor(@RequestBody @Valid ActorDTO actorDTO,
                                                  BindingResult bindingResult, @PathVariable int id) {
        Actor actor = actorService.convertToActor(actorDTO);
        actor.setId(id);
        actorValidator.validate(actor, bindingResult, true);
        if(bindingResult.hasErrors()) {
            returnErrorsToClient(bindingResult);
        }
        actorService.updateActor(actorDTO, id);
        return ResponseEntity.ok(HttpStatus.UPGRADE_REQUIRED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteActor(@PathVariable int id) {
        actorService.deleteActor(id);
        return ResponseEntity.ok(HttpStatus.UPGRADE_REQUIRED);
    }

    @GetMapping("/{id}")
    public ActorInfoDTO getActor(@PathVariable int id) {
        return actorService.getActor(id);
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
