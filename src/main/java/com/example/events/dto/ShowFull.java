package com.example.events.dto;

import com.example.events.model.Show;

import lombok.Getter;
import lombok.Setter;

/**
 * View on all show details. Joined from Movie and Cinema.
 */
@Getter
@Setter
public class ShowFull {
    private Double price;
    private String day;
    private String time;
    // Movie.title
    private String movieTitle;
    // Movie.rating
    private Double movieRating;
    // Cinema.name
    private String cinemaName;
    // Cinema.zipCode + Cinema.city + Cinema.street
    private String cinemaAddress;
    // Cinema.phone
    private String cinemaPhone;

    /**
     * Factory method that creates a ShowFull view from a given show.
     * 
     * @param show
     *            input
     * @return new ShowFull object
     */
    public static ShowFull fromShow(Show show) {
        ShowFull showFull = new ShowFull();
        showFull.setPrice(show.getPrice());
        showFull.setDay(show.getDay());
        showFull.setTime(show.getTime());
        if (show.getMovie() != null) {
            showFull.setMovieTitle(show.getMovie().getTitle());
            showFull.setMovieRating(show.getMovie().getRating());
        }
        if (show.getCinema() != null) {
            showFull.setCinemaName(show.getCinema().getName());
            showFull.setCinemaAddress(
                show.getCinema().getZipCode() + " " + show.getCinema().getCity() + ", " + show.getCinema().getStreet());
            showFull.setCinemaPhone(show.getCinema().getPhone());
        }
        return showFull;
    }
}
