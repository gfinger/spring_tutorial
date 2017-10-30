package com.example.events.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShowInput {
    private Long id;
    private Double price;
    private String day;
    private String time;
    private Long movieId;
    private Long cinemaId;
}
