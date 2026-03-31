package com.example.moviedb.services;

import com.example.moviedb.entities.Actor;
import com.example.moviedb.entities.Movie;
import com.example.moviedb.exceptions.ResourceNotFoundException;
import com.example.moviedb.repositories.ActorRepository;
import com.example.moviedb.repositories.MovieRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.*;;

// layer which carries out business logic regarding Actor
//Encapsulates database functionality
@Service
public class ActorService {

    // Dependency injection
    private final ActorRepository actorRepository;
    private final MovieRepository movieRepository;

    // Service constructor
    public ActorService(ActorRepository actorRepository, MovieRepository movieRepository) {
        this.actorRepository = actorRepository;
        this.movieRepository = movieRepository;

    }
    // find actors with id
    public Optional<Actor> findActorById(Long id) {
        return actorRepository.findById(id);
    }
    // return all actors
    public List<Actor> findAllActors(){
        return actorRepository.findAll();
    }
    // return actors using pagination
    public Page<Actor> getActors(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return actorRepository.findAll(pageable);
    }
    // searches actors by name using (case-insensitivity)
    public List<Actor> getActorsWithIgnoreCase(String name) {
        List<Actor> actors = actorRepository.findByNameContainingIgnoreCase(name);
        return actors;
    }

    // find movies with actor id
    public List<Movie> getMoviesWithActorID(Long actorID) {
        return movieRepository.findByActors_Id(actorID);
    }

    // creates actor with flexible api request body order
    //validates movie IDs, links actors/genre with movie, also updates other entities
    public Actor createActor(Map<String, Object> actorData) {
        String name = "";
        LocalDate birthDate = null;

        if (actorData.containsKey("name")) {
            name = (String) actorData.get("name");
        }
        if (actorData.containsKey("birthDate")) {
            birthDate = LocalDate.parse((String) actorData.get("birthDate"));
        }

        List<Integer> movieIds = (List<Integer>) actorData.get("movieIds");
        Set<Movie> movies = new HashSet<>();

        if (movieIds != null) {

            for (Integer movieId : movieIds) {
                movieRepository.findById(Long.valueOf(movieId)).orElseThrow(() -> new ResourceNotFoundException("Movie with that id does not exist " + movieId));
                }
            movies = new HashSet<>(movieRepository.findAllById(movieIds.stream().map(Long::valueOf).toList()));
            }

        Actor actorSaved = new Actor(name, birthDate, movies);
        if (movieIds != null) {
            for (Movie movie : movies) {
                movie.getActors().add(actorSaved);
            }
        }
        return actorRepository.save(actorSaved);

    }

    // updates existing actor's info
    public Actor updateActor(Long id, Map<String, Object> actorData) {
        Optional<Actor> actor = actorRepository.findById(id);
        if (!actor.isPresent()) {
         throw new ResourceNotFoundException("Actor with id " + id + " does not exist");
        }

        if(actorData.containsKey("name")){
            String name = (String) actorData.get("name");
            actor.get().setName(name);
        }
        if(actorData.containsKey("birthDate")){
            String birthDateInput = (String) actorData.get("birthDate");
            LocalDate birthDate = LocalDate.parse(birthDateInput);
            actor.get().setBirthDate(birthDate);
        }
        if(actorData.containsKey("movieIds")){
            List<Integer> movieIds = (List<Integer>) actorData.get("movieIds");

            for (Movie movie : new HashSet<>(actor.get().getMovies())) {
                movie.getActors().remove(actor.get());
            }
            actor.get().getMovies().clear();

            for (Integer movieId : movieIds) {
                movieRepository.findById(Long.valueOf(movieId)).orElseThrow(() -> new ResourceNotFoundException("Movie with that id does not exist " + movieId));
            }
            Set<Movie> newMovies = new HashSet<>(movieRepository.findAllById(movieIds.stream().map(Long::valueOf).toList()));

            for (Movie movie : newMovies) {
                movie.getActors().add(actor.get());
            }
            actor.get().setMovies(newMovies);
        }
        return actorRepository.save(actor.get());

    }

    //deletes actor with id
    //does not delete if actor is connected to movies and force is not used
    public void deleteActor(Long id, boolean force) {
        Optional<Actor> actor = actorRepository.findById(id);
        if (!actor.isPresent()) {
            throw new ResourceNotFoundException("Actor with id " + id + " does not exist");
        }
        if (!actor.get().getMovies().isEmpty() && !force) {
            throw new IllegalArgumentException(

                    "Unable to delete actor '" + actor.get().getName() + "' because they are connected with " + actor.get().getMovies().size() + " movies");
        }

        actorRepository.delete(actor.get());
    }



}
