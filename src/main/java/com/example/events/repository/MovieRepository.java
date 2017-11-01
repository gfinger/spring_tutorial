package com.example.events.repository;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.example.events.model.Movie;

@Component
public class MovieRepository extends BasicRepository<Movie, Long> {

    public MovieRepository(ConcurrentHashMap<Long, Movie> entities) {
        super(entities);
    }

}
