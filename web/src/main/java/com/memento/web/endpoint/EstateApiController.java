package com.memento.web.endpoint;

import com.memento.model.Estate;
import com.memento.model.Permission;
import com.memento.service.EstateService;
import com.memento.web.endpoint.api.EstateApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/estate", produces = {MediaType.APPLICATION_JSON_VALUE})
public class EstateApiController implements EstateApi {

    private final EstateService estateService;

    @Autowired
    public EstateApiController(final EstateService estateService) {
        this.estateService = estateService;
    }

    @Override
    @Secured({ Permission.Value.ADMIN, Permission.Value.AGENCY })
    @PostMapping(value = "/save")
    public ResponseEntity<Estate> save(@RequestBody @Valid final Estate estate) {
        return ResponseEntity.ok(estateService.save(estate));
    }
}
