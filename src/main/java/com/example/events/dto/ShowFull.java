package com.example.events.dto;

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
}
