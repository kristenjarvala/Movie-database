package com.example.moviedb.repositories;

import com.example.moviedb.entities.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//handles database operations for Genre entities
@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    List<Genre> findByName(String Name);
}
