package com.example.events;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.example.events.model.Cinema;
import com.example.events.model.Movie;
import com.example.events.model.Show;

@Configuration
@ComponentScan(basePackages = { "com.example.events.repository", "com.example.events.service" })
public class RootConfig {
    @Bean
    public ConcurrentHashMap<Long, Cinema> cinemaStore() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public ConcurrentHashMap<Long, Movie> movieStore() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public ConcurrentHashMap<Long, Show> showStore() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    InitializingBean storeInitializingBean(ConcurrentHashMap<Long, Cinema> cinemaStore,
            ConcurrentHashMap<Long, Movie> movieStore, ConcurrentHashMap<Long, Show> showStore) {
        return () -> {
            Cinema cinema = new Cinema();
            cinema.setId(1L);
            cinema.setName("Luxor");
            cinema.setCity("Bensheim");
            cinema.setZipCode("64625");
            cinema.setStreet("Berliner Ring 26");

            cinemaStore.put(1L, cinema);

            Movie movie = new Movie();
            movie.setId(1L);
            movie.setTitle("Blade Runner 2049");
            movie.setRating(8.5);

            movieStore.put(1L, movie);

            Show show = new Show();
            show.setId(1L);
            show.setCinema(cinema);
            show.setMovie(movie);
            show.setDay("01.11.2017");
            show.setTime("21:00");
            show.setPrice(11.50);

            showStore.put(1L, show);
        };
    }
}
