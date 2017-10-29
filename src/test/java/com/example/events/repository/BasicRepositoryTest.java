package com.example.events.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.example.events.TestConfiguration;
import com.example.events.model.Entity;

import lombok.Getter;
import lombok.Setter;

// Runs the tests using the Spring test context
@RunWith(SpringRunner.class)
// Load the test configuration into the application context
@ContextConfiguration(classes = { TestConfiguration.class })
// Inject the Spring beans where annotated
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class })
public class BasicRepositoryTest {
    @Setter
    @Getter
    private static class TestEntity implements Entity<Long> {
        Long id;
    }

    @Autowired
    private Map<Long, Entity<Long>> store;
    @Autowired
    private Repository<Entity<Long>, Long> repository;
    private Entity<Long> entity1;
    private Entity<Long> entity2;

    @Before
    public void setUp() throws Exception {
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
