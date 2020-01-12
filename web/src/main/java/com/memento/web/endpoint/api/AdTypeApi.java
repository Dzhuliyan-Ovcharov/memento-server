package com.memento.web.endpoint.api;

import com.memento.model.AdType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;

import java.util.Set;

@Api(value = "Ad type API")
public interface AdTypeApi {

    @ApiOperation(value = "Save ad type", response = AdType.class)
    ResponseEntity<AdType> save(AdType adType);

    @ApiOperation(value = "Get all ad types", response = Set.class)
    ResponseEntity<Set<AdType>> getAll();
}
