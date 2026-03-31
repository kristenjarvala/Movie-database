package com.example.moviedb.services;


import com.example.moviedb.entities.Actor;
import com.example.moviedb.entities.Genre;
import com.example.moviedb.entities.Movie;
import com.example.moviedb.exceptions.ResourceNotFoundException;
import com.example.moviedb.repositories.ActorRepository;
import com.example.moviedb.repositories.GenreRepository;
import com.example.moviedb.repositories.MovieRepository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final ActorRepository actorRepository;

    public MovieService(MovieRepository movieRepository,
                        GenreRepository genreRepository,
                        ActorRepository actorRepository) {
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
        this.actorRepository = actorRepository;
    }
    // returns all movies
    public List<Movie> findAllMovies() {
        return movieRepository.findAll();
    }
    // returns movies with pagination
    public Page<Movie> getMoviesPaged(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return movieRepository.findAll(pageable);
    }
    // finds movies with id
    public Optional<Movie> findMovieById(Long id) {
        return movieRepository.findById(id);
    }
    // find movies with genre id
    public List<Movie> getMoviesWithGenreId(Long genreID) {
        return movieRepository.findByGenres_Id(genreID);
    }

    // finds movie with name (case-insensitivity)
    public List<Movie> getMoviesWithTitle(String title) {
        List<Movie> movies = movieRepository.findByMovieNameContainingIgnoreCase(title);
        return movies;
    }
    // find movies with release year
    public List<Movie> getMoviesWithReleaseYear(int releaseYear) {
        return movieRepository.findByReleaseYear(releaseYear);
    }
    // find actors in that movie
    public List<Actor> getActorsByMovieId(Long id) {
        Optional<Movie> movie = movieRepository.findById(id);
        if (movie == null) {
            throw new ResourceNotFoundException("Movie with id " + id + " not found");

        }
        return new ArrayList<>(movie.get().getActors());
    }
    // creates a movie with random json body order
    public Movie createMovie(Map<String, Object> movieMap) {
        String movieName = (String) movieMap.get("title");
        if (movieName == null || movieName.isEmpty()) {
            throw new IllegalArgumentException("Movie name input is mandatory!");
        }
        Integer releaseYear = (Integer) movieMap.get("releaseYear");
        if (releaseYear == null || releaseYear < 1888) {
            throw new IllegalArgumentException("Release year input malformed!");
        }
        Integer duration = (Integer) movieMap.get("duration");
        if (duration == null || duration <= 1) {
            throw new IllegalArgumentException("Duration input malformed!");
        }

        List<Integer> actorIds = (List<Integer>) movieMap.get("actorIds");
        List<Integer> genreIds = (List<Integer>) movieMap.get("genreIds");

        Set<Actor> actors = new HashSet<>();
        if (actorIds != null) {
            for (Integer actorId : actorIds) {
                Optional<Actor> optionalActor = actorRepository.findById(Long.valueOf(actorId));
                if (optionalActor.isPresent()) {
                    actors.add(optionalActor.get());
                } else {
                    throw new ResourceNotFoundException("Actor not found!");
                }
            }
        }

        Set<Genre> genres = new HashSet<>();
        if (genreIds != null) {
            for (Integer genreId : genreIds) {
                Optional<Genre> optionalGenre = genreRepository.findById(Long.valueOf(genreId));
                if (optionalGenre.isPresent()) {
                    genres.add(optionalGenre.get());
                } else {
                    throw new ResourceNotFoundException("Genre not found!");
                }
            }
        }

        Movie newMovie = new Movie(movieName, releaseYear, duration, genres, actors);
        return movieRepository.save(newMovie);
    }


    // partially updates movie field and associated entities
    @Transactional
    public Movie patchMovie(Long id, Map<String, Object> updateMovieMap) {
        Optional<Movie> existing = movieRepository.findById(id);
        if (existing.isEmpty()) {
            throw new ResourceNotFoundException("Movie not found!");
        }
        Movie movie = existing.get();

        if (updateMovieMap.containsKey("title")) {
            String movieName = (String) updateMovieMap.get("title");
            if (movieName != null && !movieName.isEmpty()) {
                movie.setMovieTitle(movieName);
            }
        }

        if (updateMovieMap.containsKey("releaseYear")) {
            Integer releaseYear = (Integer) updateMovieMap.get("releaseYear");
            if (releaseYear != null && releaseYear > 1888) {
                movie.setReleaseYear(releaseYear);
            }
        }

        if (updateMovieMap.containsKey("duration")) {
            Integer duration = (Integer) updateMovieMap.get("duration");
            if (duration != null && duration > 1) {
                movie.setMovieDuration(duration);
            }
        }

        if (updateMovieMap.containsKey("actorIds")) {
            List<Integer> actorIds = (List<Integer>) updateMovieMap.get("actorIds");
            Set<Actor> actors = new HashSet<>();
            if (actorIds != null) {
                for (Integer actorId : actorIds) {
                    Optional<Actor> optionalActor = actorRepository.findById(actorId.longValue());
                    if (optionalActor.isPresent()) {
                        actors.add(optionalActor.get());
                    }
                }
                movie.setActors(actors);
            }
        }

            if (updateMovieMap.containsKey("genreIds")) {
                List<Integer> genreIds = (List<Integer>) updateMovieMap.get("genreIds");
                Set<Genre> genres = new HashSet<>();
                if (genreIds != null) {
                    for (Integer genreId : genreIds) {
                        Optional<Genre> optionalGenre = genreRepository.findById(genreId.longValue());
                        if (optionalGenre.isPresent()) {
                            genres.add(optionalGenre.get());
                        }
                    }
                }
                movie.setGenres(genres);
            }
            return movieRepository.save(movie);
        }

        // deletes movie with id
        //if force is not used and movie is associated with entities then will return an error
        @Transactional
        public void deleteMovie(Long id, boolean force) {
            Movie movie = movieRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Movie not found!"));

            if (!force && (!movie.getActors().isEmpty() || !movie.getGenres().isEmpty())) {
                throw new IllegalArgumentException("Movie is linked to actors or genres; use force delete to override.");
            }

            if (force) {
                for (Actor actor : new HashSet<>(movie.getActors())) {
                    actor.getMovies().remove(movie);
                }
                for (Genre genre : new HashSet<>(movie.getGenres())) {
                    genre.getMovies().remove(movie);
                }
                movie.getActors().clear();
                movie.getGenres().clear();
            }

            movieRepository.delete(movie);
        }


}







