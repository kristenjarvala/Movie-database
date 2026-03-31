package com.example.moviedb.repositories;

import com.example.moviedb.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//handles database operations for Movie entities
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    //find movies with case sensitivity
    List<Movie> findByMovieName(String movieName);

    // finds movies with actor id
    List<Movie> findByActors_Id(Long actorId);

    // finds movies with genre id
    List<Movie> findByGenres_Id(Long genreId);

    // finds movies with case-insensitivity
    List<Movie> findByMovieNameContainingIgnoreCase(String title);

    // finds movies with release year
    List<Movie> findByReleaseYear(Integer releaseYear);
}
