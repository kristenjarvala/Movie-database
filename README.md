
# KMDB Movies-api README

  Spring boot REST API for managing movies, genres, and actors

## technologies needed

- java 17 JDK 
- Maven
- Git
- Any IDE
- postman (for testing)

### Install Guide

1. Clone Repository
   - Open terminal and type
   - git clone https:
   - cd kmbd
2. Open The Project
   - when opening the file up in your IDE click to "import Maven Projects"
3. Project building
   - mvn clean install
4. Start the App
   - Start by clicking the Run button of your IDE 
5. Open the `Movie Database API.postman_collection.json` through postman by clicking import and going to this folder and opening the file through this
## Features

   - CRUD operations for movie, actor and genre entities
   - Search movie/actor with title/name and movie/actor with release year/birthdate
   - Search any entity with pagination support
   - Input validation for updating and creating entities
   - When deleting entities with relationship gives out error(unless force=true is used as a paremeter when deleting)
   - database file is created when running the code for the first time
### Database

  #### Usage
  - URL to access APO locally is `http://localhost:8080/api`

## Endpoints

### Actors

#### GET /actors

- Get all actors

#### GET /actors/{id}

- Get the actor with that id

#### GET /actors?name={name}

- Get actors based on the input name (case-insensitive)

#### PATCH /actors/{id}

- Update actor with that id

Example JSON body
```
"name": "Bob Actor",
"birthDate": "2005-05-16"
```

#### POST /actors

- Create a new actor

- Example JSON body

```
"name": "Actor Bob",
"birthDate": "1905-05-16"
```

#### DELETE /actors/{id}

- Deletes actor with given id, 
but if the actor is associated with movies then force=true needs to be used!

#### GET /movies

- Get all movies

#### GET /movies/{id}

- Get the movie with that id 

#### GET /movies/search?title={title}

- Get movies based on the input name (case-insensitive)

#### GET /movies?actor={actorId}

- Get movies that this actor has starred in

#### GET /movies/{id}/actors

- Get all actors from this specific movie

#### GET /movies?genre={genreId}

- Get all movies with that genre

#### POST /movies

- Creates a new movie

Example JSON body
```
"title": "The Great Escape",
"releaseYear": 1963,
"duration": 172,
"actorIds": [1, 2, 3],
"genreIds": [1, 2]
```

#### PATCH /movies/{id}

- Update movie with that id

Example JSON body
```
"title": "Girls und Panzer der Film",
"releaseYear": 2015,
"duration": 119,
"actorIds": null,
"genreIds": null

```

#### DELETE /movies/{id}

- Deletes movie with given id,
  but if the movie is associated with genres/actors then force=true needs to be used!

#### GET /genres

- Get all genres

#### GET /genres/{id}

- Get the genre with that id

#### POST /genres

- Creates a new genre

Example JSON body
```
"name" : "Adventure"
```

#### PATCH /genres/{id}

- Update genre with that id

Example JSON body
```
"name" : "Comedy"
```

#### DELETE /genres/{id}

- Deletes genre with given id,
  but if the genre is associated with movies then force=true needs to be used!
