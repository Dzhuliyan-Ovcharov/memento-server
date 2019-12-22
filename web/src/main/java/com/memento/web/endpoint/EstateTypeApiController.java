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

@RestController
@RequestMapping(value = "/api/estate/type", produces = {MediaType.APPLICATION_JSON_VALUE})
public class EstateTypeApiController implements EstateTypeApi {

    private final EstateTypeService estateTypeService;

    @Autowired
    public EstateTypeApiController(EstateTypeService estateTypeService) {
        this.estateTypeService = estateTypeService;
    }

    @Override
    @GetMapping(value = "/all")
    public ResponseEntity<Set<EstateType>> getAll() {
        return ResponseEntity.ok(estateTypeService.getAll());
    }

    @Override
    @Secured(Permission.Value.ADMIN)
    @PostMapping(value = "/save")
    public ResponseEntity<EstateType> save(@RequestBody @Valid final EstateType estateType) {
        return ResponseEntity.ok(estateTypeService.save(estateType));
    }

    @Override
    @Secured(Permission.Value.ADMIN)
    @PutMapping(value = "/update")
    public ResponseEntity<EstateType> update(@RequestBody @Valid final EstateType estateType) {
        return ResponseEntity.ok(estateTypeService.update(estateType));
    }
}
