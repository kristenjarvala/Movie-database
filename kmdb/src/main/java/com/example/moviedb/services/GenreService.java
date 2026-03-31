package com.example.moviedb.services;

import com.example.moviedb.entities.Genre;
import com.example.moviedb.entities.Movie;
import com.example.moviedb.exceptions.ResourceNotFoundException;
import com.example.moviedb.repositories.GenreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {

    //dependency injection
    private GenreRepository genreRepository;


    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    // returns all genres
    public List<Genre> findAllGenres() {
        return genreRepository.findAll();
    }

    //get all genres with pagination
    public Page<Genre> getAllGenres(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return genreRepository.findAll(pageable);
    }
    // find genre with id
    public Optional<Genre> findGenreById(Long id) {
        return genreRepository.findById(id);
    }

    // saves new or updates genre
    public Genre saveGenreById(Genre genre) {
        return genreRepository.save(genre);
    }

    // updates existing genre
    public Genre updateGenre(Long id, Genre newGenre) {
        Optional<Genre> genre = genreRepository.findById(id);
        if (!genre.isPresent()) {
            throw new ResourceNotFoundException("Genre not found!");
        }
        if(newGenre.getName() != null) {
            genre.get().setName(newGenre.getName());
        }
        return genreRepository.save(genre.get());
    }

    // deletes genre with id
    //does not delete if genre is connected to movies and force is not used
    public void deleteGenre(Long id, boolean force) {
        Optional<Genre> genre = genreRepository.findById(id);
        if (!genre.isPresent()) {
            throw new ResourceNotFoundException("Genre with id " + id + " not found!");
        }
        if(!genre.get().getMovies().isEmpty() && force == false) {
            throw new IllegalArgumentException(

                    "Unable to delete genre '" + genre.get().getName() + "' because it is connected with " + genre.get().getMovies().size() + " movies!");
        }
        genreRepository.delete(genre.get());

    }
}
