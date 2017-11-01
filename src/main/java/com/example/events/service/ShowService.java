package com.example.events.service;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.events.dto.ShowFull;
import com.example.events.dto.ShowInput;
import com.example.events.model.Cinema;
import com.example.events.model.Movie;
import com.example.events.model.Show;
import com.example.events.repository.CinemaRepository;
import com.example.events.repository.MovieRepository;
import com.example.events.repository.ShowRepository;

@Component
public class ShowService {
    private CinemaRepository cinemaRepository;
    private MovieRepository movieRepository;
    private ShowRepository showRepository;

    /**
     * Construct Show Service with required dependencies.
     * 
     * @param cinemaRepository
     *            {@link CinemaRepository}
     * @param movieRepository
     *            {@link MovieRepository}
     * @param showRepository
     *            {@link ShowRepository}
     */
    public ShowService(CinemaRepository cinemaRepository, MovieRepository movieRepository,
            ShowRepository showRepository) {
        this.cinemaRepository = cinemaRepository;
        this.movieRepository = movieRepository;
        this.showRepository = showRepository;
    }

    /**
     * Save a show from an ShowInput object.
     * 
     * @param showInput
     *            {@link ShowInput}
     */
    public void save(ShowInput showInput) {
        Movie movie = movieRepository.findById(showInput.getMovieId());
        if (movie != null) {
            Cinema cinema = cinemaRepository.findById(showInput.getCinemaId());
            if (cinema != null) {
                Show show = new Show();
                show.setMovie(movie);
                show.setCinema(cinema);
                show.setDay(showInput.getDay());
                show.setTime(showInput.getTime());
                show.setPrice(showInput.getPrice());
                show.setId(showInput.getId());
                showRepository.save(show);
            }
        }
    }

    /**
     * Find shows corresponding to a movie title.
     * Matches if the movie title starts with the input string.
     * @param movieTitle input string
     * @return all found shows matching‚
     */
    public Collection<ShowFull> findAllByMovieTitle(String movieTitle) {
        Collection<Show> shows = showRepository.findAll();
        return shows.stream().filter(show -> show.getMovie().getTitle().startsWith(movieTitle)).map(ShowFull::fromShow)
                .collect(Collectors.toList());
    }

    /**
     * Find shows corresponding to a cinema name.
     * Matches if the cinema name starts with the input string.
     * @param cinemaName input string
     * @return all found shows matching
     */
    public Collection<ShowFull> findAllByCinemaName(String cinemaName) {
        Collection<Show> shows = showRepository.findAll();
        return shows.stream().filter(show -> show.getCinema().getName().startsWith(cinemaName)).map(ShowFull::fromShow)
                .collect(Collectors.toList());
    }
    
    public ShowFull findById(Long id) {
        return ShowFull.fromShow(showRepository.findById(id));
    }
}
