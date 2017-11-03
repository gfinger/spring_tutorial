package com.example.events.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.events.model.Movie;
import com.example.events.repository.MovieRepository;

@RestController
@RequestMapping("/movie")
public class MovieController {
    private MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    /**
     * Create a movie object in the movie store.
     * 
     * @param movie
     *            {@link MovieRepository}
     * @return input object
     */
    @PostMapping("save")
    public ResponseEntity<Movie> save(@RequestBody Movie movie) {
        movieRepository.save(movie);
        return ResponseEntity
                .created(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + movie.getId()).build().toUri())
                .body(movie);
    }

    @GetMapping("{id}")
    public Movie byId(@PathVariable Long id) {
        return movieRepository.findById(id);
    }
}
