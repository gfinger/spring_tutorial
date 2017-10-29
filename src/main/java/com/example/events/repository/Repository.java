package com.example.events.repository;

import java.util.Collection;

import com.example.events.model.Entity;

public interface Repository<T extends Entity<ID>, ID> {
    void save(T entity);

    void saveAll(Collection<T> entities);

    T findById(ID id);

    Collection<T> findAll();
}
