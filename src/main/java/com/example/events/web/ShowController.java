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

import com.example.events.ObjectAlreadyExistsException;
import com.example.events.ObjectNotFoundException;
import com.example.events.dto.ShowFull;
import com.example.events.dto.ShowInput;
import com.example.events.model.Show;
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

    /**
     * Get show by id.
     * 
     * @param id
     *            id of show
     * @return ShowFull view of found show.
     * @throws ObjectNotFoundException
     *             if show with id was not found.
     */
    @GetMapping(path = "{id}")
    public ShowFull byId(@PathVariable Long id) throws ObjectNotFoundException {
        ShowFull showFull = showService.findById(id);
        if (showFull == null) {
            throw ObjectNotFoundException.builder().objectType(Show.class).id(id.toString()).build();
        }
        return showFull;
    }

    /**
     * Create a show object in the show store.
     * 
     * @param showInput
     *            {@link ShowInput}
     * @return input object
     * @throws ObjectAlreadyExistsException  if show with given id already exists.
     */
    @PostMapping(path = "save")
    public ResponseEntity<ShowInput> save(@RequestBody ShowInput showInput) throws ObjectAlreadyExistsException {
        if (showService.findById(showInput.getId()) != null) {
            throw ObjectAlreadyExistsException.builder().objectType(Show.class).id(showInput.getId().toString())
                    .build();
        }
        showService.save(showInput);
        return ResponseEntity
                .created(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + showInput.getId()).build().toUri())
                .body(showInput);
    }
}
