package com.memento.web.endpoint.api;

import com.memento.model.EstateType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;

import java.util.Set;

@Api(value = "Ad type API")
public interface EstateTypeApi {

    @ApiOperation(value = "Get all estate types", response = Set.class)
    ResponseEntity<Set<EstateType>> getAll();

    @ApiOperation(value = "Save estate type", response = EstateType.class)
    ResponseEntity<EstateType> save(EstateType estateType);

    @ApiOperation(value = "Update estate type", response = EstateType.class)
    ResponseEntity<EstateType> update(EstateType estateType);
}
