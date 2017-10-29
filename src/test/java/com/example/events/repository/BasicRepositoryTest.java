package com.example.events.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.example.events.model.Entity;

import lombok.Getter;
import lombok.Setter;

@RunWith(MockitoJUnitRunner.class)
public class BasicRepositoryTest {
    @Setter
    @Getter
    private static class TestEntity implements Entity<Long> {
        Long id;
    }

    @Mock
    private HashMap<Long, Entity<Long>> store;
    private BasicRepository<Entity<Long>, Long> repository;
    private Entity<Long> entity1;
    private Entity<Long> entity2;

    @Before
    public void setUp() throws Exception {
        // inject mocked store into repository
        repository = new BasicRepository<>(store);

        entity1 = new TestEntity();
        entity1.setId(1L);

        entity2 = new TestEntity();
        entity2.setId(2L);
    }

    @Test
    public void testSave() {
        repository.save(entity1);
        verify(store).put(1L, entity1);
        repository.save(entity2);
        verify(store).put(1L, entity1);
    }

    @Test
    public void testSaveAll() {
        List<Entity<Long>> entities = List.of(entity1, entity2);
        repository.saveAll(entities);
        verify(store).put(1L, entity1);
        verify(store).put(1L, entity1);
    }

    @Test
    public void testFindById() {
        when(store.get(1L)).thenReturn(entity1);
        when(store.get(2L)).thenReturn(entity2);
        assertThat(repository.findById(1L)).isEqualTo(entity1);
        assertThat(repository.findById(2L)).isEqualTo(entity2);
    }

    @Test
    public void testFindAll() {
        when(store.values()).thenReturn(List.of(entity1, entity2));
        assertThat(repository.findAll()).containsExactly(entity1, entity2);
    }

}
