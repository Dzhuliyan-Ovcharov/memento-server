package com.memento.web.endpoint.api;

import com.memento.web.dto.EstateRequest;
import com.memento.web.dto.EstateResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Set;

@Api(value = "Estate API")
public interface EstateApi {

    @ApiOperation(value = "Find estate by id", response = EstateResponse.class)
    ResponseEntity<EstateResponse> findById(Long id);

    @ApiOperation(value = "Fetch all estates", response = Set.class)
    ResponseEntity<Set<EstateResponse>> getAll();

    @ApiOperation(value = "Save estate", response = EstateResponse.class)
    ResponseEntity<EstateResponse> save(EstateRequest estateRequest);

    @ApiOperation(value = "Update estate", response = EstateResponse.class)
    ResponseEntity<EstateResponse> update(Long id, EstateRequest estateRequest);

    @ApiOperation(value = "Fetch all estates by user email", response = Set.class)
    ResponseEntity<Set<EstateResponse>> getEstatesByUserEmail(@PathVariable final String email);
}
