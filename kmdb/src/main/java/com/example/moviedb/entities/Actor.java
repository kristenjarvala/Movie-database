package com.example.moviedb.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity // marks this class as a JPA entity (represents a database table)
public class Actor {

    @Id // marks primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //generates id for Actor entity
    private Long id;

    @NotBlank(message = "Name input is mandatory!")
    private String name;

    @NotNull(message = "Birthdate input is mandatory!")
    @JsonFormat(pattern = "yyyy-MM-dd") //date must be formated year-month-day
    @Past(message = "Date must not be today or from the future!")
    private LocalDate birthDate;


    // default constructor
    public Actor(){}

    // custom constructor
    public Actor(
            String name,
            LocalDate birthDate,
            Set<Movie> movies) {
                setName(name);
                setBirthDate(birthDate);
                setMovies(movies);

    }

    @ManyToMany(mappedBy = "actors") // many-to-many with Movie, handled on Movie side
    @JsonIgnore // hide this field in JSON to avoid infinite recursion
    private Set<Movie> movies = new HashSet<>();

    // getters
    public Long getId() {

        return id;
    }

    public String getName() {

        return name;
    }

    public LocalDate setBirthDate() {

        return birthDate;
    }

    public Set<Movie> getMovies() {

        return this.movies;
    }
    // setters
    public void setName(String name) {

        this.name = name;
    }

    public void setBirthDate(LocalDate actorBirthDate) {
        this.birthDate = actorBirthDate;
    }
    public void setMovies(Set<Movie> movies) {

        this.movies = movies;
    }

}
