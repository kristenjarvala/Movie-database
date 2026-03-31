package com.example.moviedb.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    // custom 404 "Not Found" Exception
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
