package com.example.events.web;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;

import com.example.events.repository.CinemaRepository;
import com.example.events.service.ShowService;

public class TestWebConfig {
    @Bean
    public ShowService showService() {
        return Mockito.mock(ShowService.class);
    }
    
    @Bean
    public CinemaRepository cinemaRepository() {
        return Mockito.mock(CinemaRepository.class);
    }
}
