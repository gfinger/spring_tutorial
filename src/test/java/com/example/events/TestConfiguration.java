package com.example.events;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;

import com.example.events.model.Entity;
import com.example.events.repository.BasicRepository;
import com.example.events.repository.Repository;

/**
 * Test configuration of Spring beans.
 * To be loaded in the application context.
 */
public class TestConfiguration {

    @Bean
    Map<Long, Entity<Long>> store() {
        return new HashMap<>();
    }

    @Bean
    Repository<Entity<Long>, Long> repository(Map<Long, Entity<Long>> store) {
        return new BasicRepository<>(store);
    }
}
