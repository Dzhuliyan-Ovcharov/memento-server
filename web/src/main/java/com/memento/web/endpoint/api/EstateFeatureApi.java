package com.memento.web.endpoint.api;

import com.memento.model.EstateFeature;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;

import java.util.Set;

@Api(value = "Estate Feature API")
public interface EstateFeatureApi {

    @ApiOperation(value = "Get all estate features", response = Set.class)
    ResponseEntity<Set<EstateFeature>> getAll();

    @ApiOperation(value = "Get estate feature by feature", response = EstateFeature.class)
    ResponseEntity<EstateFeature> findByFeature(String feature);

    @ApiOperation(value = "Save estate feature", response = EstateFeature.class)
    ResponseEntity<EstateFeature> save(EstateFeature estateFeature);
}
