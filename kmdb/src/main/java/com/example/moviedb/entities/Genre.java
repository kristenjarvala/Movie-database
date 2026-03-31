package com.example.moviedb.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

@Entity // marks this class as a JPA entity (represents a database table)
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-incrementing ID
    private Long id;

    @NotBlank(message = "Name input is mandatory") // name must not be null or empty
    private String name;

    // default constructor required by JPA
    Genre(){}

    // constructor with name
    public Genre(String name) {
        setName(name);
    }


    @JsonIgnore // prevents infinite recursion during serialization
    @ManyToMany(mappedBy = "genres")
    private Set<Movie> movies = new HashSet<>();

    // getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public Set<Movie> getMovies() {
        return this.movies;
    }

    // setters
    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
    }

    public void setName(String name) {
        this.name = name;
    }

}
