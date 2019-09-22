package com.memento.web;

import com.memento.model.Floor;
import com.memento.service.FloorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping(value = "/api/floor")
public class FloorController {

    private final FloorService floorService;

    @Autowired
    public FloorController(final FloorService floorService) {
        this.floorService = floorService;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<Set<Floor>> getAll() {
        return ResponseEntity.ok(floorService.getAll());
    }

    @GetMapping(value = "/number/{number}")
    public ResponseEntity<Floor> findByNumber(@PathVariable(value = "number") final Integer number) {
        return ResponseEntity.ok(floorService.findByNumber(number));
    }

    @PostMapping(value = "/save")
    public ResponseEntity<Floor> save(@Valid @RequestBody final Floor floor) {
        return ResponseEntity.ok(floorService.save(floor));
    }
}
