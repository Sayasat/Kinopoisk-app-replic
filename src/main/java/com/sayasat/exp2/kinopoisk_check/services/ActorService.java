package com.sayasat.exp2.kinopoisk_check.services;

import com.sayasat.exp2.kinopoisk_check.dto.actors.ActorDTO;
import com.sayasat.exp2.kinopoisk_check.dto.actors.ActorInfoDTO;
import com.sayasat.exp2.kinopoisk_check.models.Actor;
import com.sayasat.exp2.kinopoisk_check.repositories.ActorRepository;
import com.sayasat.exp2.kinopoisk_check.util.MovieException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ActorService {

    public final ActorRepository actorRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ActorService(ActorRepository actorRepository, ModelMapper modelMapper) {
        this.actorRepository = actorRepository;
        this.modelMapper = modelMapper;
    }

    public List<ActorDTO> getAllActors() {
        return actorRepository.findAll().stream().map(this::convertToActorDTO)
                .collect(Collectors.toList());
    }

    public ActorInfoDTO getActor(int id) {
        if(actorRepository.existsById(id)) {
            return convertToActorInfoDTO(actorRepository.findById(id).get());
        } else {
            throw new MovieException("Actor not found");
        }
    }

    @Transactional
    public void addActor(ActorDTO actorDTO) {
        actorRepository.save(convertToActor(actorDTO));
    }

    @Transactional
    public void updateActor(ActorDTO actorDTO, int id) {
        Actor actor = actorRepository.findById(id).orElse(null);
        if (actor != null) {
            Actor actorToUpdate = convertToActor(actorDTO);
            actorToUpdate.setId(id);
            actorRepository.save(actorToUpdate);
        } else {
            throw new MovieException("Actor with that id wasn't found");
        }
    }

    @Transactional
    public void deleteActor(int id) {
        Actor actor = actorRepository.findById(id).orElse(null);
        if (actor != null) {
            actorRepository.delete(actor);
        } else {
            throw new MovieException("Actor with that id wasn't found");
        }
    }

    public Actor findActorById(int id) {
        return actorRepository.findById(id).orElse(null);
    }

    public Actor findActorByFullName(String fullName) {
        return actorRepository.findByFullName(fullName);
    }

    public Actor convertToActor(ActorDTO actorDTO) {
        return modelMapper.map(actorDTO, Actor.class);
    }

    public ActorDTO convertToActorDTO(Actor actor) {
        return modelMapper.map(actor, ActorDTO.class);
    }

    public ActorInfoDTO convertToActorInfoDTO(Actor actor) {
        return modelMapper.map(actor, ActorInfoDTO.class);
    }
}
