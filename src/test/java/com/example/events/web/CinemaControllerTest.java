package com.example.events.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.example.events.dto.ShowInput;
import com.example.events.model.Cinema;
import com.example.events.model.Movie;
import com.example.events.model.Show;
import com.example.events.repository.CinemaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(CinemaController.class)
public class CinemaControllerTest {
    @MockBean
    private CinemaRepository cinemaRepository;
    @Autowired
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
    }

    @Test
    public void testById() throws Exception {
        when(cinemaRepository.findById(cinema.getId())).thenReturn(cinema);
        mockMvc.perform(get("/cinema/1")).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.name").value(cinema.getName()));
    }

    @Test
    public void testSave() throws Exception {
        ArgumentCaptor<Cinema> captor = ArgumentCaptor.forClass(Cinema.class);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post("/cinema/save").content(objectMapper.writeValueAsString(cinema))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
        verify(cinemaRepository).save(captor.capture());
        Cinema param = captor.getValue();
        assertThat(param).isEqualToComparingFieldByField(cinema);
    }
}
