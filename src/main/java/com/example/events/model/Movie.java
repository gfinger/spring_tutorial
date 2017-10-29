package com.example.events.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Movie implements Entity<Long> {
    private Long id;
    private String title;
    private Double rating;
    private String imdbId;
    private String image;
    private String year;
}
