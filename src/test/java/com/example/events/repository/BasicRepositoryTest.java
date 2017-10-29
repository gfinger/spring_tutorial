package com.example.events.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.example.events.model.Entity;

import lombok.Getter;
import lombok.Setter;

public class BasicRepositoryTest {
    @Setter
    @Getter
    private static class TestEntity implements Entity<Long> {
        Long id;
    }

    private HashMap<Long, Entity<Long>> store;
    private BasicRepository<Entity<Long>, Long> repository;
    private Entity<Long> entity1;
    private Entity<Long> entity2;

    @Before
    public void setUp() throws Exception {
        store = new HashMap<>();

        // inject store into repository
        repository = new BasicRepository<>(store);

        entity1 = new TestEntity();
        entity1.setId(1L);

        entity2 = new TestEntity();
        entity2.setId(2L);
    }

    @Test
    public void testSave() {
        repository.save(entity1);
        assertThat(store).containsEntry(1L, entity1);
        repository.save(entity2);
        assertThat(store).containsEntry(2L, entity2);
        assertThat(store).hasSize(2);
    }

    @Test
    public void testSaveAll() {
        List<Entity<Long>> entities = List.of(entity1, entity2);
        repository.saveAll(entities);
        assertThat(store).containsEntry(1L, entity1);
        assertThat(store).containsEntry(2L, entity2);
        assertThat(store).hasSize(2);
    }

    @Test
    public void testFindById() {
        store.putAll(Map.of(1L, entity1, 2L, entity2));
        assertThat(repository.findById(1L)).isEqualTo(entity1);
        assertThat(repository.findById(2L)).isEqualTo(entity2);
    }

    @Test
    public void testFindAll() {
        store.putAll(Map.of(1L, entity1, 2L, entity2));
        assertThat(repository.findAll()).containsExactly(entity1, entity2);
    }

}
