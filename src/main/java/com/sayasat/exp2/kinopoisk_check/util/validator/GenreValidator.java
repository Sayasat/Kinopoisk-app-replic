package com.sayasat.exp2.kinopoisk_check.util.validator;

import com.sayasat.exp2.kinopoisk_check.models.Genre;
import com.sayasat.exp2.kinopoisk_check.services.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class GenreValidator implements Validator {

    private final GenreService genreService;

    @Autowired
    public GenreValidator(final GenreService genreService) {
        this.genreService = genreService;
    }
    @Override
    public boolean supports(Class<?> clazz) {
        return Genre.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Genre genre = (Genre) target;

        if(genreService.findByGenre(genre.getGenre()).isPresent())
            errors.rejectValue("genre", "", "Genre already exists");
    }
}
