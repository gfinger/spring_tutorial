package com.example.events.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.net.URI;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.example.events.model.Cinema;
import com.example.events.repository.CinemaRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CinemaControllerRealIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

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
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        RequestEntity<Cinema> requestEntity = new RequestEntity<Cinema>(cinema, headers, HttpMethod.POST,
                new URI("/cinema/save"));
        restTemplate.exchange(requestEntity, Cinema.class);
        verify(cinemaRepository, times(1)).save(any(Cinema.class));
    }

    @Test
    public void testById() throws Exception {
        ResponseEntity<Cinema> response = restTemplate.exchange("/cinema/{id}", HttpMethod.GET, null, Cinema.class,
            cinema.getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualByComparingTo(MediaType.APPLICATION_JSON_UTF8);
        Cinema body = response.getBody();
        assertThat(body).isEqualToComparingFieldByField(cinema);
    }

}
