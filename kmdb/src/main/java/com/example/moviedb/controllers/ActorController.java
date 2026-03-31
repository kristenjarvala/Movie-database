package com.example.moviedb.controllers;

import com.example.moviedb.entities.Actor;
import com.example.moviedb.exceptions.ResourceNotFoundException;
import com.example.moviedb.services.ActorService;
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

@RestController // this tells Spring that this class handles HTTP requests and returns data (not HTML)
@RequestMapping("/actors") // all routes in this controller start with /actors
@Validated // allows validation rules (like @Min, @Max) to be applied to method parameters
public class ActorController {

    private final ActorService actorService;

    // constructor that Spring uses to give this controller an instance of ActorService
    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }


    // GET /actors?page=...&size=...
    // returns a page of actors, with validation on page and size parameters
    @GetMapping(params = "page")
    public ResponseEntity<Page<Actor>> getActorsWithPagination(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(value = 100, message = "Maximum size is 100") int size) {
        Page<Actor> actors = actorService.getActors(page, size);
        return ResponseEntity.ok(actors);
    }

    // GET /actors?name=someName
    // searches for actors whose name contains the given text, case-insensitive
    @GetMapping(params = "name")
    public ResponseEntity<?> getActorsWithName(@RequestParam("name") String name) {
        return ResponseEntity.ok().body(actorService.getActorsWithIgnoreCase(name));
    }


    // GET /actors/{id}
    // fetches a single actor by their ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getActorById(@PathVariable Long id) {
        Optional<Actor> actor = actorService.findActorById(id);
        if (!actor.isPresent()) {
            throw new ResourceNotFoundException("Actor with id " + id + " not found");
        }
        return ResponseEntity.ok().body(actor.get());
    }

    // GET /actors
    // returns all actors without any filters or pagination
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<Actor> getAllActors() {
        return actorService.findAllActors();
    }

    // POST /actors
    // creates a new actor using the data sent in the request body
    @PostMapping
    public ResponseEntity<Actor> postActor(@RequestBody Map<String, Object> actorEntity) {
        Actor actor = actorService.createActor(actorEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(actor);
    }

    // PATCH /actors/{id}
    // updates parts of an existing actor using the fields sent in the request body
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateActor(@PathVariable Long id, @RequestBody Map<String, Object> actorEntity) {
        return ResponseEntity.ok().body(actorService.updateActor(id, actorEntity));
    }

    // DELETE /actors/{id}?force=true
    // deletes an actor by ID; if force=true, also removes links to movies
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteActor(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean force) {
        actorService.deleteActor(id, force);
        return ResponseEntity.noContent().build();

    }

}
