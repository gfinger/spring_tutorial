package com.example.events.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import com.example.events.ObjectAlreadyExistsException;
import com.example.events.ObjectNotFoundException;
import com.example.events.WebConfig;
import com.example.events.dto.ShowFull;
import com.example.events.dto.ShowInput;
import com.example.events.model.Cinema;
import com.example.events.model.Movie;
import com.example.events.model.Show;
import com.example.events.service.ShowService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { WebConfig.class, TestWebConfig.class })
public class ShowControllerTest {
    @Autowired
    private ShowService showService;
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    private Cinema cinema;
    private Movie movie;
    private Show show;
    private ShowInput showInput;

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

        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void testShowByMovieTitle() throws Exception {
        when(showService.findAllByMovieTitle(movie.getTitle())).thenReturn(List.of(ShowFull.fromShow(show)));
        mockMvc.perform(get("/show/byMovieTitle").param("title", movie.getTitle())).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$[0].movieTitle").value(movie.getTitle()));
    }

    @Test
    public void testShowByCinemaName() throws Exception {
        when(showService.findAllByCinemaName(cinema.getName())).thenReturn(List.of(ShowFull.fromShow(show)));
        mockMvc.perform(get("/show/byCinemaName").param("name", cinema.getName())).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$[0].cinemaName").value(cinema.getName()));
    }

    @Test
    public void testFindById() throws Exception {
        when(showService.findById(show.getId())).thenReturn(ShowFull.fromShow(show));
        mockMvc.perform(get("/show/1")).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.price").value(show.getPrice()));
    }

    @Test(expected = NestedServletException.class)
    public void testFindByIdException() throws Exception {
        when(showService.findById(0L)).thenReturn(null);
        try {
            mockMvc.perform(get("/show/0")).andExpect(status().isInternalServerError());
        } catch (NestedServletException servletException) {
            Object cause = servletException.getCause();
            assertThat(cause).isExactlyInstanceOf(ObjectNotFoundException.class);
            assertThat(cause).hasFieldOrPropertyWithValue("id", "0");
            assertThat(cause).hasFieldOrPropertyWithValue("objectType", Show.class);
            throw servletException;
        }
    }

    @Test
    public void testSaveShow() throws Exception {
        ArgumentCaptor<ShowInput> captor = ArgumentCaptor.forClass(ShowInput.class);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post("/show/save").content(objectMapper.writeValueAsString(showInput))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
        verify(showService).save(captor.capture());
        ShowInput param = captor.getValue();
        assertThat(param).isEqualToComparingFieldByField(showInput);
    }

    @Test(expected = NestedServletException.class)
    public void testSaveException() throws Exception {
        when(showService.findById(showInput.getId())).thenReturn(ShowFull.fromShow(show));
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            mockMvc.perform(post("/show/save").content(objectMapper.writeValueAsString(showInput))
                    .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError());
        } catch (NestedServletException servletException) {
            Object cause = servletException.getCause();
            assertThat(cause).isExactlyInstanceOf(ObjectAlreadyExistsException.class);
            assertThat(cause).hasFieldOrPropertyWithValue("id", "1");
            assertThat(cause).hasFieldOrPropertyWithValue("objectType", Show.class);
            throw servletException;
        }
    }
}
