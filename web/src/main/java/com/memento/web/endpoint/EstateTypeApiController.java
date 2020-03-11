package com.memento.web.endpoint;

import com.memento.model.EstateType;
import com.memento.model.Permission;
import com.memento.service.EstateTypeService;
import com.memento.web.endpoint.api.EstateTypeApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

import static com.memento.web.RequestUrlConstant.ESTATE_TYPES_BASE_URL;

@RestController
@RequestMapping(value = ESTATE_TYPES_BASE_URL, produces = {MediaType.APPLICATION_JSON_VALUE})
public class EstateTypeApiController implements EstateTypeApi {

    private final EstateTypeService estateTypeService;

    @Autowired
    public EstateTypeApiController(final EstateTypeService estateTypeService) {
        this.estateTypeService = estateTypeService;
    }

    @Override
    @GetMapping
    public ResponseEntity<Set<EstateType>> getAll() {
        return ResponseEntity.ok(estateTypeService.getAll());
    }

    @Override
    @Secured(Permission.Value.ADMIN)
    @PostMapping
    public ResponseEntity<EstateType> save(@RequestBody @Valid final EstateType estateType) {
        return ResponseEntity.ok(estateTypeService.save(estateType));
    }

    @Override
    @Secured(Permission.Value.ADMIN)
    @PutMapping(value = "/{id}")
    public ResponseEntity<EstateType> update(@PathVariable final Long id, @RequestBody @Valid final EstateType estateType) {
        return ResponseEntity.ok(estateTypeService.update(id, estateType));
    }

    @Override
    @GetMapping(value = "/type/{type}")
    public ResponseEntity<EstateType> findByType(@PathVariable(value = "type") final String type) {
        return ResponseEntity.ok(estateTypeService.findByType(type));
    }
}
