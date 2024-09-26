package com.sayasat.exp2.kinopoisk_check.services;

import com.sayasat.exp2.kinopoisk_check.dto.movies.MovieActorDTO;
import com.sayasat.exp2.kinopoisk_check.dto.movies.MovieDTO;
import com.sayasat.exp2.kinopoisk_check.dto.movies.MovieInfoDTO;
import com.sayasat.exp2.kinopoisk_check.dto.movies.MovieRatingDTO;
import com.sayasat.exp2.kinopoisk_check.models.*;
import com.sayasat.exp2.kinopoisk_check.repositories.ActorRepository;
import com.sayasat.exp2.kinopoisk_check.repositories.MovieRatingRepository;
import com.sayasat.exp2.kinopoisk_check.repositories.MovieRepository;
import com.sayasat.exp2.kinopoisk_check.repositories.UserRepository;
import com.sayasat.exp2.kinopoisk_check.util.MovieException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MovieService {

    private final MovieRepository movieRepository;
    private final DirectorService directorService;
    private final GenreService genreService;
    private final ModelMapper modelMapper;
    private final ActorRepository actorRepository;
    private final MovieRatingRepository movieRatingRepository;
    private final UserRepository userRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository, DirectorService directorService,
                        GenreService genreService, ModelMapper modelMapper, ActorRepository actorRepository, MovieRatingRepository movieRatingRepository, UserRepository userRepository) {
        this.movieRepository = movieRepository;
        this.directorService = directorService;
        this.genreService = genreService;
        this.modelMapper = modelMapper;
        this.actorRepository = actorRepository;
        this.movieRatingRepository = movieRatingRepository;
        this.userRepository = userRepository;
    }

    public List<MovieDTO> getAllMovies() {
        return movieRepository.findAll().stream().map(this::convertToMovieDTO)
                .collect(Collectors.toList());
    }

    public MovieInfoDTO getMovie(int id) {
        Optional<Movie> movie = movieRepository.findById(id);
        if (movie.isPresent()) {
            return convertToMovieWIthActorsDTO(movie.get());
        } else {
            throw new MovieException("Movie not found");
        }
    }

    @Transactional
    public void save(MovieDTO movieDTO) {
        Director director = directorService.findOrCreateDirector(movieDTO.getDirector());

        Movie movie = convertToMovie(movieDTO);
        movie.setDirector(director);

        Set<Genre> genres = genreService.convertGenres(movieDTO.getGenres());
        movie.setGenres(genres);

        movieRepository.save(movie);
    }

    @Transactional
    public void update(MovieDTO movieDTO, int id) {
        Optional<Movie> optionalMovie = movieRepository.findById(id);
        if(optionalMovie.isPresent()) {
            Movie movie = convertToMovie(movieDTO);
            Director director = directorService.findOrCreateDirector(movieDTO.getDirector());
            movie.setDirector(director);
            Set<Genre> genres = genreService.convertGenres(movieDTO.getGenres());
            movie.setGenres(genres);
            movie.setId(id);
            movieRepository.save(movie);
        }else {
            throw new MovieException("Movie not found");
        }
    }

    @Transactional
    public void delete(int id) {
        Optional<Movie> optionalMovie = movieRepository.findById(id);
        if(optionalMovie.isPresent()) {
            Movie movie = optionalMovie.get();
            movieRepository.delete(movie);
        }else {
            throw new MovieException("Movie with that id wasn't found");
        }
    }

    @Transactional
    public void addActor(MovieActorDTO dto) {
        Movie movie = movieRepository.findById(dto.getMovie())
                .orElseThrow(() -> new MovieException("Movie not found"));
        Actor actor = actorRepository.findById(dto.getActor())
                .orElseThrow(() -> new MovieException("Actor not found"));
        movie.getActors().add(actor);

        movieRepository.save(movie);
        // Сохранение объекта
    }

    public List<MovieDTO> searchMoviesByTitle(String search) {
        return movieRepository.findByTitleContainingIgnoreCase(search).stream().map(this::convertToMovieDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void rateMovie(int movieId, double newRating, String username) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieException("Movie not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new MovieException("User not found"));

        MovieRating existingRating = movieRatingRepository.findByMovieAndUser(movie, user);

        if (existingRating != null) {
            // Если пользователь уже оценил фильм, обновляем рейтинг
            movie.setRating((movie.getRating() * movie.getNumberOfVotes() - existingRating.getRating() + newRating) / movie.getNumberOfVotes());
            existingRating.setRating(newRating);
            movieRatingRepository.save(existingRating);
        } else {
            // Если это первая оценка пользователя для данного фильма
            MovieRating movieRating = new MovieRating();
            movieRating.setMovie(movie);
            movieRating.setUser(user);
            movieRating.setRating(newRating);
            movieRatingRepository.save(movieRating);

            // Обновляем рейтинг и количество голосов
            movie.setRating((movie.getRating() * movie.getNumberOfVotes() + newRating) / (movie.getNumberOfVotes() + 1));
            movie.setNumberOfVotes(movie.getNumberOfVotes() + 1);
        }

        movieRepository.save(movie);
    }

    public Movie convertToMovie(MovieRatingDTO movieRatingDTO) {
        return modelMapper.map(movieRatingDTO, Movie.class);
    }

    public Movie convertToMovie(MovieDTO movieDTO) {
        return modelMapper.map(movieDTO, Movie.class);
    }

    public MovieDTO convertToMovieDTO(Movie movie) {
        return modelMapper.map(movie, MovieDTO.class);
    }

    public MovieInfoDTO convertToMovieWIthActorsDTO(Movie movie) {
        return modelMapper.map(movie, MovieInfoDTO.class);
    }
}
