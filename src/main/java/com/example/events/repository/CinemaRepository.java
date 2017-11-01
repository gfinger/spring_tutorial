package com.example.events.repository;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.example.events.model.Cinema;

@Component
public class CinemaRepository extends BasicRepository<Cinema, Long> {

    public CinemaRepository(ConcurrentHashMap<Long, Cinema> entities) {
        super(entities);
    }

}
