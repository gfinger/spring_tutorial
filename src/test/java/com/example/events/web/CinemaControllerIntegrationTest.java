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

import com.example.events.model.Cinema;
import com.example.events.repository.CinemaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class CinemaControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private CinemaRepository cinemaRepository;

    private Cinema cinema;

    @Before
    public void setUp() throws Exception {
        cinema = new Cinema();
        cinema.setId(1L);
        cinema.setName("Luxor");
        cinema.setCity("Bensheim");
        cinema.setZipCode("64625");
        cinema.setStreet("Berliner Ring 26");
    }

    @Test
    public void testSave() throws Exception {
        cinema.setId(2L);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post("/cinema/save").content(objectMapper.writeValueAsString(cinema))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
        verify(cinemaRepository, times(1)).save(any(Cinema.class));
    }

    @Test
    public void testById() throws Exception {
        mockMvc.perform(get("/cinema/1")).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.name").value(cinema.getName()));
    }

}
