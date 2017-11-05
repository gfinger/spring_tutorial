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

import com.example.events.model.Movie;
import com.example.events.repository.MovieRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MovieControllerRealIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

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
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        RequestEntity<Movie> requestEntity = new RequestEntity<Movie>(movie, headers, HttpMethod.POST,
                new URI("/movie/save"));
        restTemplate.exchange(requestEntity, Movie.class);
        verify(movieRepository, times(1)).save(any(Movie.class));
    }

    @Test
    public void testById() throws Exception {
        ResponseEntity<Movie> response = restTemplate.exchange("/movie/{id}", HttpMethod.GET, null, Movie.class,
            movie.getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualByComparingTo(MediaType.APPLICATION_JSON_UTF8);
        Movie body = response.getBody();
        assertThat(body).isEqualToComparingFieldByField(movie);
    }

}
