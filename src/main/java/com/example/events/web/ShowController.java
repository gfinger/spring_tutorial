package com.example.events.web;

import java.util.Collection;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.events.dto.ShowFull;
import com.example.events.dto.ShowInput;
import com.example.events.service.ShowService;

@RestController
@RequestMapping("/show")
public class ShowController {

    private ShowService showService;

    /**
     * Constructs the controller with needed dependencies.
     * 
     * @param showService
     *            {@link ShowService}
     */
    public ShowController(ShowService showService) {
        this.showService = showService;
    }

    @GetMapping(path = "byMovieTitle")
    public Collection<ShowFull> byMovieTitle(@RequestParam String title) {
        return showService.findAllByMovieTitle(title);
    }

    @GetMapping(path = "byCinemaName")
    public Collection<ShowFull> byCinemaName(@RequestParam String name) {
        return showService.findAllByCinemaName(name);
    }
    
    @GetMapping(path = "{id}")
    public ShowFull byId(@PathVariable Long id) {
        return showService.findById(id);
    }

    /**
     * Create a show object in the show store.
     * 
     * @param showInput
     *            {@link ShowInput}
     * @return input object
     */
    @PostMapping(path = "save")
    public ResponseEntity<ShowInput> save(@RequestBody ShowInput showInput) {
        showService.save(showInput);
        return ResponseEntity
                .created(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + showInput.getId()).build().toUri())
                .body(showInput);
    }
}
