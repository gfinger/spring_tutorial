package com.example.events.web;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;

import com.example.events.service.ShowService;

public class TestWebConfig {
    @Bean
    public ShowService showService() {
        return Mockito.mock(ShowService.class);
    }
}
