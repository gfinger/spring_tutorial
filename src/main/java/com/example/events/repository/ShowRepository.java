package com.example.events.repository;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.example.events.model.Show;

@Component
public class ShowRepository extends BasicRepository<Show, Long> {

    public ShowRepository(ConcurrentHashMap<Long, Show> entities) {
        super(entities);
    }

}
