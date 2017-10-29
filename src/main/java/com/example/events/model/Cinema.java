package com.example.events.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cinema implements Entity<Long> {
    private Long id;
    private String name;
    private String phone;
    private String imdbId;
    private String street;
    private String city;
    private String zipCode;
}
