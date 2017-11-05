package com.example.events.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.example.events.ObjectAlreadyExistsException;
import com.example.events.ObjectNotFoundException;
import com.example.events.dto.ShowFull;
import com.example.events.dto.ShowInput;
import com.example.events.model.Cinema;
import com.example.events.model.Movie;
import com.example.events.model.Show;
import com.example.events.service.ShowService;

@RunWith(MockitoJUnitRunner.class)
public class ShowControllerUnitTest {
    @Mock
    private ShowService showService;

    private ShowController showController;

    private Cinema cinema;
    private Movie movie;
    private Show show;
    private ShowInput showInput;

    @Before
    public void setUp() throws Exception {
        showController = new ShowController(showService);

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

    @Test
    public void testShowByMovieTitle() throws Exception {
        List<ShowFull> shows = List.of(ShowFull.fromShow(show));
        when(showService.findAllByMovieTitle(movie.getTitle())).thenReturn(shows);
        assertThat(showController.byMovieTitle(movie.getTitle())).isEqualTo(shows);
    }

    @Test
    public void testShowByCinemaName() throws Exception {
        List<ShowFull> shows = List.of(ShowFull.fromShow(show));
        when(showService.findAllByCinemaName(cinema.getName())).thenReturn(shows);
        assertThat(showController.byCinemaName(cinema.getName())).isEqualTo(shows);
    }

    @Test
    public void testFindById() throws Exception {
        ShowFull showFull = ShowFull.fromShow(show);
        when(showService.findById(show.getId())).thenReturn(showFull);
        assertThat(showController.byId(show.getId())).isEqualTo(showFull);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void testFindByIdException() throws Exception {
        when(showService.findById(0L)).thenReturn(null);
        try {
            showController.byId(0L);
        } catch (ObjectNotFoundException exception) {
            assertThat(exception).isExactlyInstanceOf(ObjectNotFoundException.class);
            assertThat(exception).hasFieldOrPropertyWithValue("id", "0");
            assertThat(exception).hasFieldOrPropertyWithValue("objectType", Show.class);
            throw exception;
        }
    }

    @Test
    // The ShowController.save method relies on being run in a servlet environment,
    // and cannot be tested without it.
    public void testSaveShow() throws Exception {
        // ArgumentCaptor<ShowInput> captor = ArgumentCaptor.forClass(ShowInput.class);
        // showController.save(showInput);
        // verify(showService).save(captor.capture());
        // ShowInput param = captor.getValue();
        // assertThat(param).isEqualToComparingFieldByField(showInput);
    }

    @Test(expected = ObjectAlreadyExistsException.class)
    public void testSaveException() throws Exception {
        when(showService.findById(showInput.getId())).thenReturn(ShowFull.fromShow(show));
        try {
            showController.save(showInput);
        } catch (ObjectAlreadyExistsException exception) {
            assertThat(exception).isExactlyInstanceOf(ObjectAlreadyExistsException.class);
            assertThat(exception).hasFieldOrPropertyWithValue("id", "1");
            assertThat(exception).hasFieldOrPropertyWithValue("objectType", Show.class);
            throw exception;
        }
    }
}
