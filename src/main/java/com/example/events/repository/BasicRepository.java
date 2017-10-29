package com.example.events.repository;

import java.util.Collection;
import java.util.Map;

import com.example.events.model.Entity;

public class BasicRepository<T extends Entity<ID>, ID> implements Repository<T, ID> {
    private Map<ID, T> store;
    
    // expect store to be set from the outside
    public BasicRepository(Map<ID, T> store) {
        this.store = store;
    }

    @Override
    public void save(T entity) {
        store.put(entity.getId(), entity);
    }

    @Override
    public void saveAll(Collection<T> entities) {
        entities.forEach(entity -> this.store.put(entity.getId(), entity));
    }

    @Override
    public T findById(ID id) {
        return store.get(id);
    }

    @Override
    public Collection<T> findAll() {
        return store.values();
    }

}
