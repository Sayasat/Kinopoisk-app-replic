package com.sayasat.exp2.kinopoisk_check.services;

import com.sayasat.exp2.kinopoisk_check.dto.directors.DirectorDTO;
import com.sayasat.exp2.kinopoisk_check.dto.directors.DirectorNameDTO;
import com.sayasat.exp2.kinopoisk_check.models.Director;
import com.sayasat.exp2.kinopoisk_check.repositories.DirectorRepository;
import com.sayasat.exp2.kinopoisk_check.util.MovieException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DirectorService {

    private final DirectorRepository directorRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public DirectorService(DirectorRepository directorRepository, ModelMapper modelMapper) {
        this.directorRepository = directorRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public Director findOrCreateDirector(DirectorNameDTO directorNameDTO) {
        Optional<Director> existingDirector = directorRepository.findByFullName(directorNameDTO.getFullName());

        if (existingDirector.isPresent()) {
            Director existing = existingDirector.get();
            return directorRepository.save(existing);
        } else {
            Director newDirector = new Director(directorNameDTO.getFullName(),
                    "",
                    LocalDate.now());
            return directorRepository.save(newDirector);
        }
    }

    public Optional<Director> findByFullName(String fullName) {
        return directorRepository.findByFullName(fullName);
    }

    public Optional<Director> findById(int id) {
        return directorRepository.findById(id);
    }

    @Transactional
    public void addDirector(DirectorDTO directorDTO) {
        directorRepository.save(converToDirector(directorDTO));
    }

    public List<DirectorDTO> getAllDirectors() {
        return directorRepository.findAll().stream().map(this::converToDirectorDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void update(DirectorDTO directorDTO, int id) {
        Optional<Director> existingDirector = directorRepository.findById(id);
        if (existingDirector.isPresent()) {
            Director director = converToDirector(directorDTO);
            director.setId(id);
            directorRepository.save(director);
        } else {
            throw new MovieException("Director with that id wasn't found");
        }
    }

    @Transactional
    public void delete(int id) {
        Optional<Director> existingDirector = directorRepository.findById(id);
        if (existingDirector.isPresent()) {
            Director director = existingDirector.get();
            directorRepository.delete(director);
        } else {
            throw new MovieException("Director with that id wasn't found");
        }
    }

    public Director converToDirector(DirectorDTO directorDTO) {
        return modelMapper.map(directorDTO, Director.class);
    }

    public DirectorDTO converToDirectorDTO(Director director) {
        return modelMapper.map(director, DirectorDTO.class);
    }
}
