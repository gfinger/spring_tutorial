package com.example.events.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Show implements Entity<Long> {
    private Long id;
    private Double price;
    private String day;
    private String time;
    private Movie movie;
    private Cinema cinema;
}
