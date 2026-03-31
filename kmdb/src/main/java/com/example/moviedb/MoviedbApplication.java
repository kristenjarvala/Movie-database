package com.example.moviedb;

import com.example.moviedb.entities.Actor;
import com.example.moviedb.entities.Genre;
import com.example.moviedb.entities.Movie;
import com.example.moviedb.repositories.ActorRepository;
import com.example.moviedb.repositories.GenreRepository;
import com.example.moviedb.repositories.MovieRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//Main
@SpringBootApplication
public class MoviedbApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoviedbApplication.class, args);
	}

	//Creates a new database file if no database file exists or is empty
	@Bean
	public CommandLineRunner insertDatabase(MovieRepository movieRepository, ActorRepository actorRepository, GenreRepository genreRepository) {
		return args -> {
			if (genreRepository.count() > 0 || actorRepository.count() > 0 || movieRepository.count() > 0) return;

			// GENRES
			Genre drama = new Genre("Drama");
			Genre action = new Genre("Action");
			Genre comedy = new Genre("Comedy");
			Genre sciFi = new Genre("Sci-Fi");
			Genre thriller = new Genre("Thriller");

			genreRepository.saveAll(List.of(drama, action, comedy, sciFi, thriller));

			// ACTORS
			Actor a1 = new Actor("Robert De Niro", LocalDate.of(1943, 8, 17), new HashSet<>());
			Actor a2 = new Actor("Meryl Streep", LocalDate.of(1949, 6, 22), new HashSet<>());
			Actor a3 = new Actor("Leonardo DiCaprio", LocalDate.of(1974, 11, 11), new HashSet<>());
			Actor a4 = new Actor("Scarlett Johansson", LocalDate.of(1984, 11, 22), new HashSet<>());
			Actor a5 = new Actor("Tom Hanks", LocalDate.of(1956, 7, 9), new HashSet<>());
			Actor a6 = new Actor("Natalie Portman", LocalDate.of(1981, 6, 9), new HashSet<>());
			Actor a7 = new Actor("Brad Pitt", LocalDate.of(1963, 12, 18), new HashSet<>());
			Actor a8 = new Actor("Cate Blanchett", LocalDate.of(1969, 5, 14), new HashSet<>());
			Actor a9 = new Actor("Christian Bale", LocalDate.of(1974, 1, 30), new HashSet<>());
			Actor a10 = new Actor("Amy Adams", LocalDate.of(1974, 8, 20), new HashSet<>());
			Actor a11 = new Actor("Joaquin Phoenix", LocalDate.of(1974, 10, 28), new HashSet<>());
			Actor a12 = new Actor("Jennifer Lawrence", LocalDate.of(1990, 8, 15), new HashSet<>());
			Actor a13 = new Actor("Denzel Washington", LocalDate.of(1954, 12, 28), new HashSet<>());
			Actor a14 = new Actor("Viola Davis", LocalDate.of(1965, 8, 11), new HashSet<>());
			Actor a15 = new Actor("Ryan Gosling", LocalDate.of(1980, 11, 12), new HashSet<>());

			actorRepository.saveAll(List.of(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15));

			// MOVIES
			List<Movie> movies = List.of(
					new Movie("Inception", 2010, 148, Set.of(sciFi, action), Set.of(a3, a7)),
					new Movie("The Godfather Part II", 1974, 202, Set.of(drama), Set.of(a1)),
					new Movie("Forrest Gump", 1994, 142, Set.of(drama, comedy), Set.of(a5)),
					new Movie("Black Swan", 2010, 108, Set.of(drama, thriller), Set.of(a6)),
					new Movie("The Avengers", 2012, 143, Set.of(action, sciFi), Set.of(a4, a7)),
					new Movie("Her", 2013, 126, Set.of(sciFi, drama), Set.of(a11, a4)),
					new Movie("Silver Linings Playbook", 2012, 122, Set.of(comedy, drama), Set.of(a12, a15)),
					new Movie("Fences", 2016, 139, Set.of(drama), Set.of(a13, a14)),
					new Movie("The Departed", 2006, 151, Set.of(thriller, drama), Set.of(a3, a7)),
					new Movie("Arrival", 2016, 116, Set.of(sciFi, drama), Set.of(a10)),
					new Movie("The Dark Knight", 2008, 152, Set.of(action, thriller), Set.of(a9)),
					new Movie("Blue Jasmine", 2013, 98, Set.of(drama), Set.of(a8)),
					new Movie("Doubt", 2008, 104, Set.of(drama), Set.of(a2, a14)),
					new Movie("Catch Me If You Can", 2002, 141, Set.of(drama, comedy), Set.of(a3, a5)),
					new Movie("La La Land", 2016, 128, Set.of(drama, comedy), Set.of(a12, a15)),
					new Movie("American Hustle", 2013, 138, Set.of(drama, comedy), Set.of(a3, a10)),
					new Movie("Gladiator", 2000, 155, Set.of(action, drama), Set.of(a9)),
					new Movie("No Country for Old Men", 2007, 122, Set.of(thriller, drama), Set.of(a7)),
					new Movie("The Iron Lady", 2011, 105, Set.of(drama), Set.of(a2)),
					new Movie("Philadelphia", 1993, 125, Set.of(drama), Set.of(a5, a13))
			);

			movieRepository.saveAll(movies);
		};
	}



}
