package com.memento.web;

import com.memento.model.City;
import com.memento.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping(value = "/api/city")
public class CityController {

    private final CityService cityService;

    @Autowired
    public CityController(final CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<Set<City>> getAll() {
        return ResponseEntity.ok(cityService.getAll());
    }

    @PostMapping(value = "/save")
    public ResponseEntity<City> save(@Valid @RequestBody final City city) {
        return ResponseEntity.ok(cityService.save(city));
    }

    @PutMapping(value = "/update")
    public ResponseEntity<City> update(@Valid @RequestBody final City city) {
        return ResponseEntity.ok(cityService.update(city));
    }
}
