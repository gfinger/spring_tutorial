package com.example.events.repository;

import java.util.concurrent.ConcurrentHashMap;

import com.example.events.model.Movie;

public class MovieRepository extends BasicRepository<Movie, Long> {

    public MovieRepository(ConcurrentHashMap<Long, Movie> entities) {
        super(entities);
    }

}
