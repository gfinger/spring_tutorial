package com.example.events.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

import com.example.events.model.Movie;
import com.example.events.repository.MovieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class MovieControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private MovieRepository movieRepository;

    private Movie movie;

    @Before
    public void setUp() throws Exception {
        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Blade Runner 2049");
        movie.setRating(8.5);
    }

    @Test
    public void testSave() throws Exception {
        movie.setId(2L);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(
            post("/movie/save").content(objectMapper.writeValueAsString(movie)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        verify(movieRepository, times(1)).save(any(Movie.class));
    }

    @Test
    public void testById() throws Exception {
        mockMvc.perform(get("/movie/1")).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.title").value(movie.getTitle()));
    }

}
