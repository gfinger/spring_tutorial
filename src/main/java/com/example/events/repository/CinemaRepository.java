package com.example.events.repository;

import java.util.concurrent.ConcurrentHashMap;

import com.example.events.model.Cinema;

public class CinemaRepository extends BasicRepository<Cinema, Long> {

    public CinemaRepository(ConcurrentHashMap<Long, Cinema> entities) {
        super(entities);
    }

}
