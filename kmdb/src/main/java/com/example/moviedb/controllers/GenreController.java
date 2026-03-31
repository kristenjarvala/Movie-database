package com.example.moviedb.controllers;

import com.example.moviedb.entities.Genre;
import com.example.moviedb.exceptions.ResourceNotFoundException;
import com.example.moviedb.services.GenreService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController // marks this class as a REST controller that handles HTTP requests
@RequestMapping("/genres") // all routes here will start with /genres
@Validated // enables validation on parameters like page/size using annotations
public class GenreController {

    private final GenreService genreService;

    // constructor that Spring uses to inject the GenreService
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    // GET /genres?page=...&size=...
    // returns a paginated list of genres; size must be between 1 and 100
    @GetMapping(params = "page")
    public ResponseEntity<Page<Genre>> getGenres(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(value = 100, message = "Max size is 100") int size){

        Page<Genre> genres = genreService.getAllGenres(page, size);
        return ResponseEntity.ok(genres);
    }

    // GET /genres/{id}
    // finds a genre by its ID, returns 404 if not found
    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Genre> getGenreWithId(@PathVariable Long id){
        Optional<Genre> genreEntity = genreService.findGenreById(id);
        if (!genreEntity.isPresent()) {
            throw new ResourceNotFoundException("Genre with id " + id + " not found");
        }
        return ResponseEntity.ok(genreEntity.get());
    }

    // GET /genres
    // returns all genres without pagination or filtering
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<Genre> getAllGenres(){

        return genreService.findAllGenres();
    }


    // POST /genres
    // creates a new genre using the request body
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Genre createGenre( @RequestBody Genre genre){

        return genreService.saveGenreById(genre);
    }

    // PATCH /genres/{id}
    // updates an existing genre partially (e.g., just the name)
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateGenre(@PathVariable Long id, @RequestBody Genre genre){
        Genre updatedGenre = genreService.updateGenre(id, genre);
        return ResponseEntity.ok(updatedGenre);
    }

    // DELETE /genres/{id}?force=true
    // deletes a genre by ID; force=true means remove all associations too
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGenre(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean force){
        genreService.deleteGenre(id, force);
        return ResponseEntity.noContent().build();
    }

}
