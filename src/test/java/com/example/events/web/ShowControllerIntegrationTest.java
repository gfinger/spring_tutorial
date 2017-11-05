package com.example.events.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

import com.example.events.ObjectAlreadyExistsException;
import com.example.events.ObjectNotFoundException;
import com.example.events.dto.ShowInput;
import com.example.events.model.Cinema;
import com.example.events.model.Movie;
import com.example.events.model.Show;
import com.example.events.repository.ShowRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class ShowControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    
    @SpyBean
    private ShowRepository showRepository;

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
    }

    @Test
    public void testShowByMovieTitle() throws Exception {
        mockMvc.perform(get("/show/byMovieTitle").param("title", movie.getTitle())).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$[0].movieTitle").value(movie.getTitle()));
    }

    @Test
    public void testShowByCinemaName() throws Exception {
        mockMvc.perform(get("/show/byCinemaName").param("name", cinema.getName())).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$[0].cinemaName").value(cinema.getName()));
    }

    @Test
    public void testFindById() throws Exception {
        mockMvc.perform(get("/show/1")).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.price").value(show.getPrice()));
    }

    @Test(expected = NestedServletException.class)
    public void testFindByIdException() throws Exception {
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
        showInput.setId(2L);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post("/show/save").content(objectMapper.writeValueAsString(showInput))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
        verify(showRepository, times(1)).save(any(Show.class));
    }

    @Test(expected = NestedServletException.class)
    public void testSaveException() throws Exception {
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
