package com.example.events.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.net.URI;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.example.events.dto.ShowFull;
import com.example.events.dto.ShowInput;
import com.example.events.model.Cinema;
import com.example.events.model.Movie;
import com.example.events.model.Show;
import com.example.events.repository.ShowRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ShowControllerRealIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @SpyBean
    private ShowRepository showRepository;

    private Cinema cinema;
    private Movie movie;
    private Show show;
    private ShowInput showInput;

    private ParameterizedTypeReference<Collection<ShowFull>> showsTypeReference = new ParameterizedTypeReference<>() {
    };

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
        ResponseEntity<Collection<ShowFull>> response = restTemplate.exchange("/show/byMovieTitle?title={title}",
            HttpMethod.GET, null, showsTypeReference, movie.getTitle());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualByComparingTo(MediaType.APPLICATION_JSON_UTF8);
        Collection<ShowFull> shows = response.getBody();
        assertThat(shows).isNotEmpty();
        assertThat(shows.stream().allMatch(show -> show.getMovieTitle().equals(movie.getTitle())));
    }

    @Test
    public void testShowByCinemaName() throws Exception {
        ResponseEntity<Collection<ShowFull>> response = restTemplate.exchange("/show/byCinemaName?name={name}",
            HttpMethod.GET, null, showsTypeReference, cinema.getName());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualByComparingTo(MediaType.APPLICATION_JSON_UTF8);
        Collection<ShowFull> shows = response.getBody();
        assertThat(shows).isNotEmpty();
        assertThat(shows.stream().allMatch(show -> show.getCinemaName().equals(cinema.getName())));
    }

    @Test
    public void testFindById() throws Exception {
        ResponseEntity<ShowFull> response = restTemplate.exchange("/show/{id}", HttpMethod.GET, null, ShowFull.class,
            show.getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualByComparingTo(MediaType.APPLICATION_JSON_UTF8);
        ShowFull body = response.getBody();
        assertThat(body).isEqualToComparingFieldByField(ShowFull.fromShow(show));
    }

    @Test
    public void testFindByIdException() throws Exception {
        ResponseEntity<String> response = restTemplate.exchange("/show/{id}", HttpMethod.GET, null, String.class, 0);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        String body = response.getBody();
        assertThat(body).contains("object of type com.example.events.model.Show with id 0 not found");
    }

    @Test
    public void testSaveShow() throws Exception {
        showInput.setId(2L);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        RequestEntity<ShowInput> requestEntity = new RequestEntity<ShowInput>(showInput, headers, HttpMethod.POST,
                new URI("/show/save"));
        restTemplate.exchange(requestEntity, ShowInput.class);
        verify(showRepository, times(1)).save(any(Show.class));
    }

    @Test
    public void testSaveException() throws Exception {
        showInput.setId(1L);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        RequestEntity<ShowInput> requestEntity = new RequestEntity<ShowInput>(showInput, headers, HttpMethod.POST,
                new URI("/show/save"));
        ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
        verify(showRepository, times(0)).save(any(Show.class));
        String body = response.getBody();
        assertThat(body).contains("object of type com.example.events.model.Show with id 1 already exists");
    }
}
