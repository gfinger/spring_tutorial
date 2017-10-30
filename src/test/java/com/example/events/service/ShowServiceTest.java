package com.example.events.service;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.example.events.model.Movie;
import com.example.events.model.Show;
import com.example.events.repository.BasicRepository;
import com.example.events.repository.MovieRepository;
import com.example.events.repository.Repository;
import com.example.events.repository.ShowRepository;

/**
 * Unit tests of show service.
 */
// Runs the tests using the Spring test context
@RunWith(SpringRunner.class)
@ContextConfiguration
// Inject the Spring beans where annotated
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class })
public class ShowServiceTest {
    @Configuration
    static class TestConfiguration {

        @Bean
        ConcurrentHashMap<Long, Show> showStore() {
            return new ConcurrentHashMap<>();
        }

        @Bean
        ShowRepository showRepository(ConcurrentHashMap<Long, Show> showStore) {
            return new ShowRepository(showStore);
        }

        @Bean
        ConcurrentHashMap<Long, Movie> movieStore() {
            return new ConcurrentHashMap<>();
        }
        
        /*
         * Define beans for MovieRepository, CinemaStore, CinemaRepository and ShowService.
         * Inject stores in repositories and repositories in service.  
         */
    }

    /**
     * Common initialization to be executed before every test.
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * Test method for {@link com.example.events.service.ShowService#save(com.example.events.dto.ShowInput)}.
     */
    @Test
    public void testSave() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.example.events.service.ShowService#findAllByMovieTitle(java.lang.String)}.
     */
    @Test
    public void testFindAllByMovieTitle() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.example.events.service.ShowService#findAllByCinemaName(java.lang.String)}.
     */
    @Test
    public void testFindAllByCinemaName() {
        fail("Not yet implemented");
    }

}
