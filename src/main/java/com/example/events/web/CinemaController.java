package com.example.events.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.events.model.Cinema;
import com.example.events.repository.CinemaRepository;

@RestController
@RequestMapping("/cinema")
public class CinemaController {
    private CinemaRepository cinemaRepository;

    public CinemaController(CinemaRepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
    }

    /**
     * Create a cinema object in the cinema store.
     * 
     * @param cinema
     *            {@link CinemaRepository}
     * @return input object
     */
    @PostMapping("save")
    public ResponseEntity<Cinema> save(@RequestBody Cinema cinema) {
        cinemaRepository.save(cinema);
        return ResponseEntity
                .created(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + cinema.getId()).build().toUri())
                .body(cinema);
    }

    @GetMapping("{id}")
    public Cinema byId(@PathVariable Long id) {
        return cinemaRepository.findById(id);
    }
}
