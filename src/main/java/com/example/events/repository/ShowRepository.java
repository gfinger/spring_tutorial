package com.example.events.repository;

import java.util.concurrent.ConcurrentHashMap;

import com.example.events.model.Show;

public class ShowRepository extends BasicRepository<Show, Long> {

    public ShowRepository(ConcurrentHashMap<Long, Show> entities) {
        super(entities);
    }

}
