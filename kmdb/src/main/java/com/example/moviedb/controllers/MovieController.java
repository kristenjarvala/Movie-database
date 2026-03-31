package com.example.moviedb.controllers;


import com.example.moviedb.entities.Movie;
import com.example.moviedb.exceptions.ResourceNotFoundException;
import com.example.moviedb.services.ActorService;
import com.example.moviedb.services.GenreService;
import com.example.moviedb.services.MovieService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController // tells Spring this class handles REST HTTP requests
@RequestMapping("/movies") // all endpoints here will start with /movies
@Validated  // enables validation on method parameters like @Min, @Max
public class MovieController {

    private final MovieService movieService;
    private final ActorService actorService;
    private final GenreService genreService;

    // constructor-based injection: Spring gives this controller access to needed services
    public MovieController(MovieService movieService, ActorService actorService, GenreService genreService) {
        this.movieService = movieService;
        this.actorService = actorService;
        this.genreService = genreService;
    }

    // GET /movies?page=...&size=...
    // returns a paginated list of movies
    @GetMapping(params = "page")
    public ResponseEntity<Page<Movie>> getMovies(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(value = 100, message = "Max Size is 100") int size) {

        Page<Movie> movies = movieService.getMoviesPaged(page, size);
        return ResponseEntity.ok(movies);
    }

    // GET /movies/{id}
    // returns one movie by ID, or 404 if not found
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieWithId(@PathVariable Long id) {
        Optional<Movie> movieOptional = movieService.findMovieById(id);
        if (!movieOptional.isPresent()) {
            throw new ResourceNotFoundException("Movie with id " + id + " not found");
        }
        return ResponseEntity.ok(movieOptional.get());
    }

    // GET /movies
    // returns all movies (no filters, no pagination)
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Movie> getMovies() {
        return movieService.findAllMovies();
    }

    // GET /movies/search?title=...
    // returns movies that match the given title (or part of it)
    @GetMapping("/search")
    public ResponseEntity<?> searchMovie(@RequestParam(required = false) String title) {
        return ResponseEntity.ok().body(movieService.getMoviesWithTitle(title));
    }

    // GET /movies/{id}/actors
    // returns actors for a specific movie
    @GetMapping("/{id}/actors")
    public ResponseEntity<?> getActors(@PathVariable Long id) {
        return ResponseEntity.ok().body(movieService.getActorsByMovieId(id));


    }

    // GET /movies?genre=..
    // filters movies by genre ID
    @GetMapping(params = "genre")
    public ResponseEntity<?> filterWithGenre(@RequestParam Long genre) {
        return ResponseEntity.ok().body(movieService.getMoviesWithGenreId(genre));
    }

    // GET /movies?year=...
    // filters movies by release year
    @GetMapping(params = "year")
    public ResponseEntity<?> filterWithYear(@RequestParam(required = false) Integer year) {
        return ResponseEntity.ok().body(movieService.getMoviesWithReleaseYear(year));
    }

    // GET /movies?actor=...
    // filters movies that contain the given actor ID
    @GetMapping(params = "actor")
    public ResponseEntity<?> filterWithActor(@RequestParam Long actor) {
        return ResponseEntity.ok().body(actorService.getMoviesWithActorID(actor));
    }

    // POST /movies
    // creates a new movie from the request body map
    @PostMapping()
    public ResponseEntity<Movie> addMovie(@Valid @RequestBody Map<String, Object> movieEntity) {
        Movie movie = movieService.createMovie(movieEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(movie);
    }
    // PATCH /movies/{id}
    // updates fields in an existing movie (partial update)
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateMovie(@PathVariable Long id, @RequestBody Map<String, Object> movieEntity) {
        return ResponseEntity.ok(movieService.patchMovie(id, movieEntity));
    }
    // DELETE /movies/{id}?force=true
    // deletes a movie, optionally also removing linked actors/genres if force=true
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean force) {
        movieService.deleteMovie(id, force);
        return ResponseEntity.noContent().build();
    }




}
