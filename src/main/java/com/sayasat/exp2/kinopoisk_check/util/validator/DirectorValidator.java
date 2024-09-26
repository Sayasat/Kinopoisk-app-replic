package com.sayasat.exp2.kinopoisk_check.util.validator;

import com.sayasat.exp2.kinopoisk_check.models.Director;
import com.sayasat.exp2.kinopoisk_check.services.DirectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class DirectorValidator implements Validator {

    private final DirectorService directorService;

    @Autowired
    public DirectorValidator(DirectorService directorService) {
        this.directorService = directorService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Director.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
    }

    public void validate(Object target, Errors errors, boolean isUpdate) {
        Director director = (Director) target;

        if(isUpdate) {
            Optional<Director> directorBefore = directorService.findById(director.getId());
            if(directorBefore.isPresent()) {
                Optional<Director> directorAfter = directorService.findByFullName(director.getFullName());
                if(directorAfter.isPresent()) {
                    if(!directorAfter.get().getFullName().equals(directorBefore.get().getFullName())) {
                        errors.rejectValue("fullName", "","Director already exists");
                    }
                }
            }
        } else {
            if(directorService.findByFullName(director.getFullName()).isPresent()) {
                errors.rejectValue("fullName", "","Director already exists");
            }
        }
    }
}
