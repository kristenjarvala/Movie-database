package com.example.moviedb.exceptions;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

// this class handles errors globally, so we don't have to repeat the same logic in every controller
@ControllerAdvice
public class GlobalExceptionHandler {

    // handles bad date format input (for example not "YYYY-MM-DD")
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handleDateTimeParseException(DateTimeParseException e) {
        return ResponseEntity.badRequest().body("Invalid birth date, must be in the format YYYY-MM-DD!");
    }

    // handles custom "not found" 404 exceptions, for example movie or actor not found so
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This entity does not exist!");
    }

    // handles generic illegal arguments (for example invalid enum string)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e) {
        Map<String, String> errors = Map.of("error", "Invalid input: " + e.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    // handles validation errors on query params or path variables (for example @Min, @Max)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e) {
        Map<String, String> errors = e.getConstraintViolations()
                .stream().collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        violation -> violation.getMessage(),
                        (existing, replacement) -> existing));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // handles wrong data types in request params (for example passing a string instead of an int for page number)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        Map<String, String> paginationError = new HashMap<>();
        paginationError.put("Error", "Input for pagination must be an integer!");
        return new ResponseEntity<>(paginationError, HttpStatus.BAD_REQUEST);
    }

    //handles illegal cast types like instead of Integer it's String
    @ExceptionHandler(ClassCastException.class)
    public ResponseEntity<String> handleClassCastException(ClassCastException e) {
        String errors = "Invalid casting type: " + e.getMessage();
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // handles illegal api handle for example instead of api/movies it's api/cars
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, String>> handleNoHandlerFoundException(NoHandlerFoundException e) {
        Map<String, String> handlerError = new HashMap<>();
        handlerError.put("Error", "No handler found for the requested resource");
        return new ResponseEntity<>(handlerError, HttpStatus.NOT_FOUND);
    }
    // handles validation errors from @Valid on @RequestBody (e.g. missing or bad fields in POST body)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> fieldErrors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        err -> err.getField(),
                        err -> err.getDefaultMessage(),
                        (msg1, msg2) -> msg1 // keep first if duplicate
                ));

        Map<String, Object> response = new HashMap<>();
        response.put("error", "Validation failed");
        response.put("details", fieldErrors);

        return ResponseEntity.badRequest().body(response);
    }




}
