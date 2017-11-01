package com.example.events.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.example.events.dto.ShowFull;
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
@RunWith(MockitoJUnitRunner.class)
public class ShowServiceTest {
    @Mock
    private CinemaRepository cinemaRepository;
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private ShowRepository showRepository;

    @Captor
    private ArgumentCaptor<Show> showCaptor;

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
        // inject mocked repositories into service
        showService = new ShowService(cinemaRepository, movieRepository, showRepository);

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
     * Cleares stores after tests.
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link com.example.events.service.ShowService#save(com.example.events.dto.ShowInput)}.
     */
    @Test
    public void testSave() {
        when(cinemaRepository.findById(1L)).thenReturn(cinema);
        when(movieRepository.findById(1L)).thenReturn(movie);
        showService.save(showInput);
        // The show service creates its own show object, which we want to capture to test it.
        verify(showRepository).save(showCaptor.capture());
        assertThat(showCaptor.getValue()).isEqualToComparingFieldByField(show);
    }

    /**
     * Test method for {@link com.example.events.service.ShowService#findAllByMovieTitle(java.lang.String)}.
     */
    @Test
    public void testFindAllByMovieTitle() {
        when(showRepository.findAll()).thenReturn(List.of(show));
        assertThat(showService.findAllByMovieTitle("Blade")).allSatisfy(showFull -> {
            assertThat(showFull.getMovieTitle()).isEqualTo("Blade Runner 2049");
        });
    }

    /**
     * Test method for {@link com.example.events.service.ShowService#findAllByCinemaName(java.lang.String)}.
     */
    @Test
    public void testFindAllByCinemaName() {
        when(showRepository.findAll()).thenReturn(List.of(show));
        assertThat(showService.findAllByCinemaName("Luxor")).allSatisfy(showFull -> {
            assertThat(showFull.getCinemaName()).isEqualTo("Luxor");
        });
    }

    /**
     * Test method for {@link com.example.events.service.ShowService#findById(java.lang.Long)}.
     */
    public void testFindById() {
        when(showRepository.findById(show.getId())).thenReturn(show);
        assertThat(showService.findById(show.getId()))
                .isEqualToComparingFieldByFieldRecursively(ShowFull.fromShow(show));
    }
}
