package com.example.moviedb.entities;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.HashSet;
import java.util.Set;

@JsonPropertyOrder({"id", "movieTitle", "releaseYear", "movieDuration", "genres", "actors"})
@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank(message = "Name input is mandatory!")
    private String movieName;

    // year the movie was released, must be 1888 or later
    @NotNull(message = "Release date input is mandatory!")
    @Min(value = 1888, message = "First movie 'Roundhay Garden Scene' came out in 1888, so anything before that is not possible.")
    private Integer releaseYear;

    // movie runtime in minutes, must be positive and within realistic max length
    @NotNull(message = "Movie duration input is mandatory!")
    @Positive
    @Max(value = 873, message = "Longest movie ever made is 873 minutes long 'Resan (The Journey)', if a longer movie has come out then please report to admin of this anomality")
    private Integer movieDuration;

    // many-to-many relationship with genres (e.g. Action, Drama)
    @ManyToMany
    @JoinTable(
            name = "movie_genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    // many-to-many relationship with actors
    @ManyToMany
    @JoinTable(
            name = "movie_actors",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    private Set<Actor> actors = new HashSet<>();

    // default constructor
    public Movie() {}

    // custom constructor
    public Movie(
            String movieTitle,
            Integer releaseYear,
            Integer movieDuration,
            Set<Genre> genres,
            Set<Actor> actors) {
                setMovieTitle(movieTitle);
                setReleaseYear(releaseYear);
                setMovieDuration(movieDuration);
                setGenres(genres);
                setActors(actors);
    }

    // setters
    public Long getId() {
        return id;
    }
    public String getMovieTitle() {
        return movieName;
    }
    public Integer getReleaseYear() {
        return releaseYear;
    }
    public Integer getMovieDuration() {
        return movieDuration;
    }
    public Set<Genre> getGenres() {
        return genres;
    }
    public Set<Actor> getActors() {
        return actors;
    }

    //getters
    public void setMovieTitle(String movieTitle) {
        this.movieName = movieTitle;
    }
    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }
    public void setMovieDuration(Integer movieDuration) {
        this.movieDuration = movieDuration;
    }
    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }
    public void setActors(Set<Actor> actors) {
        this.actors = actors;
    }
}
