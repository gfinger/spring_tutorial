package com.example.events.service;

import static org.junit.Assert.fail;

import java.util.concurrent.ConcurrentHashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.example.events.dto.ShowInput;
import com.example.events.model.Cinema;
import com.example.events.model.Movie;
import com.example.events.model.Show;
import com.example.events.repository.CinemaRepository;
import com.example.events.repository.MovieRepository;
import com.example.events.repository.ShowRepository;

/**
 * Unit tests of show service.
 */
// Runs the tests using the Spring test context
@RunWith(SpringRunner.class)
@ContextConfiguration
// Inject the Spring beans where annotated
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class })
@SuppressWarnings("unused")
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

        @Bean
        MovieRepository movieRepository(ConcurrentHashMap<Long, Movie> movieStore) {
            return new MovieRepository(movieStore);
        }

        @Bean
        ConcurrentHashMap<Long, Cinema> cinemaStore() {
            return new ConcurrentHashMap<>();
        }

        @Bean
        CinemaRepository cinemaRepository(ConcurrentHashMap<Long, Cinema> cinemaStore) {
            return new CinemaRepository(cinemaStore);
        }

        @Bean
        ShowService showService(CinemaRepository cinemaRepository, MovieRepository movieRepository,
                ShowRepository showRepository) {
            return new ShowService(cinemaRepository, movieRepository, showRepository);
        }
    }
    
    @Autowired
    private ConcurrentHashMap<Long, Cinema> cinemaStore;
    @Autowired
    private ConcurrentHashMap<Long, Movie> movieStore;
    @Autowired
    private ConcurrentHashMap<Long, Show> showStore;
    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private ShowRepository showRepository;
    @Autowired
    private ShowService showService;
    
    private Cinema cinema;
    private Movie movie;
    private Show show;
    private ShowInput showInput;
    
    /**
     * Common initialization to be executed before every test.
     */
    @Before
    public void setUp() throws Exception {
        cinema = new Cinema();
        cinema.setId(1L);
        cinema.setName("Luxor");
        cinema.setCity("Bensheim");
        cinema.setZipCode("64625");
        cinema.setStreet("Berliner Ring 26");

        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Blade Runner 2049");
        movie.setRating(8.5);

        show = new Show();
        show.setId(1L);
        show.setCinema(cinema);
        show.setMovie(movie);
        show.setDay("01.11.2017");
        show.setTime("21:00");
        show.setPrice(11.50);

        showInput = new ShowInput();
        showInput.setId(1L);
        showInput.setCinemaId(1L);
        showInput.setMovieId(1L);
        showInput.setDay("01.11.2017");
        showInput.setTime("21:00");
        showInput.setPrice(11.50);
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
