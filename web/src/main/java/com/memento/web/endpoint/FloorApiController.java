package com.memento.web.endpoint;

import com.memento.model.Floor;
import com.memento.service.FloorService;
import com.memento.web.endpoint.api.FloorApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

import static com.memento.web.RequestUrlConstant.FLOORS_BASE_URL;

@RestController
@RequestMapping(value = FLOORS_BASE_URL, produces = {MediaType.APPLICATION_JSON_VALUE})
public class FloorApiController implements FloorApi {

    private final FloorService floorService;

    @Autowired
    public FloorApiController(final FloorService floorService) {
        this.floorService = floorService;
    }

    @Override
    @GetMapping
    public ResponseEntity<Set<Floor>> getAll() {
        return ResponseEntity.ok(floorService.getAll());
    }

    @Override
    @GetMapping(params = "number")
    public ResponseEntity<Floor> findByNumber(@RequestParam(value = "number") final Integer number) {
        return ResponseEntity.ok(floorService.findByNumber(number));
    }

    @Override
    @PostMapping
    public ResponseEntity<Floor> save(@Valid @RequestBody final Floor floor) {
        return ResponseEntity.ok(floorService.save(floor));
    }
}
