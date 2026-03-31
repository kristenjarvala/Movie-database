package com.example.moviedb.repositories;

import com.example.moviedb.entities.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//lets us interact with the database table for Actor entities
// extends JpaRepository so we get basic CRUD methods
@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {
    //  lets us search for actors by name, ignoring case (e.g. "brad" matches "Brad Pitt")
    List<Actor> findByNameContainingIgnoreCase(String name);

}
