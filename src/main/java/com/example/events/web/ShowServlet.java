package com.example.events.web;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.events.dto.ShowFull;
import com.example.events.model.Cinema;
import com.example.events.model.Movie;
import com.example.events.model.Show;
import com.example.events.repository.CinemaRepository;
import com.example.events.repository.MovieRepository;
import com.example.events.repository.ShowRepository;
import com.example.events.service.ShowService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShowServlet extends HttpServlet {
    private ShowService showService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("doGet invoked");
        
        // create a Jackson object mapper that will serialize the responses
        ObjectMapper objectMapper = new ObjectMapper();
        
        Collection<ShowFull> foundShows = null;
        
        // read the path from the request url
        String path = req.getPathInfo();
        log.info("path: {}", path);
        
        // dispatch the request depending on the path
        switch (path) {
        case "/showByMovieTitle":
            // read the url parameter "title"
            String movieTitle = req.getParameter("title");
            // validate the title
            if (movieTitle != null && !movieTitle.isEmpty()) {
                foundShows = showService.findAllByMovieTitle(movieTitle);
            }
            break;
        case "/showByCinemaName":
            // read the url parameter "name"
            String cinemaName = req.getParameter("name");
            // validate the name
            if (cinemaName != null && !cinemaName.isEmpty()) {
                foundShows = showService.findAllByCinemaName(cinemaName);
            }
            break;
        default:
            break;
        }
        if (foundShows != null) {
            log.info("number of shows found {}", foundShows.size());
            // ask Jackson to serialize the collection of found shows to the body of the response
            objectMapper.writeValue(resp.getOutputStream(), foundShows);
            // set response status
            resp.setStatus(200);
        } else {
            log.info("bad request");
            // handle errors
            resp.sendError(400, "bad request");
        }
    }

    @Override
    public void init() throws ServletException {
        ConcurrentHashMap<Long, Cinema> cinemaStore = new ConcurrentHashMap<>();
        ConcurrentHashMap<Long, Movie> movieStore = new ConcurrentHashMap<>();
        ConcurrentHashMap<Long, Show> showStore = new ConcurrentHashMap<>();

        CinemaRepository cinemaRepository = new CinemaRepository(cinemaStore);
        MovieRepository movieRepository = new MovieRepository(movieStore);
        ShowRepository showRepository = new ShowRepository(showStore);

        showService = new ShowService(cinemaRepository, movieRepository, showRepository);

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
    }

    private static final long serialVersionUID = 6822424559732629973L;

}
