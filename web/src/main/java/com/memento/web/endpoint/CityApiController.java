package com.memento.web.endpoint;

import com.memento.model.City;
import com.memento.model.Permission;
import com.memento.service.CityService;
import com.memento.web.endpoint.api.CityApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

import static com.memento.web.RequestUrlConstant.CITIES_BASE_URL;

@RestController
@RequestMapping(value = CITIES_BASE_URL, produces = { MediaType.APPLICATION_JSON_VALUE })
public class CityApiController implements CityApi {

    private final CityService cityService;

    @Autowired
    public CityApiController(final CityService cityService) {
        this.cityService = cityService;
    }

    @Override
    @GetMapping
    public ResponseEntity<Set<City>> getAll() {
        return ResponseEntity.ok(cityService.getAll());
    }

    @Override
    @Secured(value = Permission.Value.ADMIN)
    @PostMapping
    public ResponseEntity<City> save(@Valid @RequestBody final City city) {
        return ResponseEntity.ok(cityService.save(city));
    }

    @Override
    @Secured(value = Permission.Value.ADMIN)
    @PutMapping(value = "/{id}")
    public ResponseEntity<City> update(@Valid @PathVariable final Long id, @RequestBody final City city) {
        return ResponseEntity.ok(cityService.update(id, city));
    }

    @Override
    @GetMapping(value = "/{id}")
    public ResponseEntity<City> findById(@PathVariable final Long id) {
        return ResponseEntity.ok(cityService.findById(id));
    }
}
