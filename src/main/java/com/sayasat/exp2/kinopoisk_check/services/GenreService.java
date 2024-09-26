package com.sayasat.exp2.kinopoisk_check.services;

import com.sayasat.exp2.kinopoisk_check.dto.genres.GenreCheckMovie;
import com.sayasat.exp2.kinopoisk_check.dto.genres.GenreDTO;
import com.sayasat.exp2.kinopoisk_check.models.Genre;
import com.sayasat.exp2.kinopoisk_check.repositories.GenreRepository;
import com.sayasat.exp2.kinopoisk_check.util.GenreNotFoundException;
import com.sayasat.exp2.kinopoisk_check.util.MovieException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GenreService {

    private final GenreRepository genreRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public GenreService(GenreRepository genreRepository, ModelMapper modelMapper) {
        this.genreRepository = genreRepository;
        this.modelMapper = modelMapper;
    }

    public List<GenreDTO> getAllGenres() {
        return genreRepository.findAll().stream().map(this::convertToGenreDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addGenre(GenreDTO genreDTO) {
        genreRepository.save(convertToGenre(genreDTO));
    }

    public Optional<Genre> findByGenre(String genre) {
        return genreRepository.findByGenre(genre);
    }

    @Transactional
    public void updateGenre(GenreDTO genreDTO, int id) {
        Optional<Genre> genre = genreRepository.findById(id);
        if (genre.isPresent()) {
            Genre genreToUpdate = convertToGenre(genreDTO);
            genreToUpdate.setId(id);
            genreRepository.save(genreToUpdate);
        } else {
            throw new MovieException("Genre with that id wasn't found");
        }
    }

    @Transactional
    public void deleteGenre(int id) {
        Optional<Genre> genre = genreRepository.findById(id);
        if (genre.isPresent()) {
            genreRepository.deleteById(id);
        } else {
            throw new MovieException("Genre with that id wasn't found");
        }
    }

    public List<GenreCheckMovie> findGenresByName(String genre) {
        return genreRepository.findByGenreStartingWith(genre).stream()
                .map(this::convertToGenreCheckMovie).collect(Collectors.toList());
    }

    public Set<Genre> convertGenres(Set<GenreDTO> genreDTOs) {
        Set<Genre> genres = new HashSet<>();
        for (GenreDTO genreDTO : genreDTOs) {
            Genre genre = genreRepository.findByGenre(genreDTO.getGenre())
                    .orElseThrow(() -> new GenreNotFoundException("Genre with name '" + genreDTO.getGenre() + "' not found"));
            genres.add(genre);
        }
        return genres;
    }

    public Genre convertToGenre(GenreDTO genreDTO) {
        return modelMapper.map(genreDTO, Genre.class);
    }

    public GenreDTO convertToGenreDTO(Genre genre) {
        return modelMapper.map(genre, GenreDTO.class);
    }

    public GenreCheckMovie convertToGenreCheckMovie(Genre genre) {
        return modelMapper.map(genre, GenreCheckMovie.class);
    }
}
