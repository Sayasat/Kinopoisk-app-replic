package com.sayasat.exp2.kinopoisk_check.util.validator;

import com.sayasat.exp2.kinopoisk_check.models.Actor;
import com.sayasat.exp2.kinopoisk_check.services.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ActorValidator implements Validator {

    private final ActorService actorService;

    @Autowired
    public ActorValidator(ActorService actorService) {
        this.actorService = actorService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Actor.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
    }

    public void validate(Object target, Errors errors, boolean isUpdate) {
        Actor actor = (Actor) target;

        if(isUpdate) {
            Actor actorBefore = actorService.findActorById(actor.getId());
            if(actorBefore != null) {
                Actor actorAfter = actorService.findActorByFullName(actor.getFullName());
                if(actorAfter != null) {
                    if(!actorAfter.getFullName().equals(actorBefore.getFullName())) {
                        errors.rejectValue("fullName", "","Actor already exists");
                    }
                }
            }
        } else {
            if(actorService.findActorByFullName(actor.getFullName()) != null) {
                errors.rejectValue("fullName", "","Director already exists");
            }
        }
    }
}
