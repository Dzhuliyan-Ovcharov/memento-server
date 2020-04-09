package com.memento.web.endpoint;

import com.memento.model.EstateFeature;
import com.memento.model.Permission;
import com.memento.service.EstateFeatureService;
import com.memento.web.endpoint.api.EstateFeatureApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

import static com.memento.web.RequestUrlConstant.ESTATE_FEATURES_BASE_URL;

@RestController
@RequestMapping(value = ESTATE_FEATURES_BASE_URL, produces = {MediaType.APPLICATION_JSON_VALUE})
public class EstateFeatureApiController implements EstateFeatureApi {

    private final EstateFeatureService estateFeatureService;

    @Autowired
    public EstateFeatureApiController(final EstateFeatureService estateFeatureService) {
        this.estateFeatureService = estateFeatureService;
    }

    @Override
    @GetMapping
    public ResponseEntity<Set<EstateFeature>> getAll() {
        return ResponseEntity.ok(estateFeatureService.getAll());
    }

    @Override
    @GetMapping(value = "/feature/{feature}")
    public ResponseEntity<EstateFeature> findByFeature(@PathVariable(value = "feature") final String feature) {
        return ResponseEntity.ok(estateFeatureService.findByFeature(feature));
    }

    @Override
    @Secured(value = Permission.Value.ADMIN)
    @PostMapping
    public ResponseEntity<EstateFeature> save(@RequestBody @Valid final EstateFeature estateFeature) {
        return ResponseEntity.ok(estateFeatureService.save(estateFeature));
    }
}
