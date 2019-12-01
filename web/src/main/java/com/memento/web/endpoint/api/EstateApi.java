package com.memento.web.endpoint.api;

import com.memento.model.Estate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;

@Api(value = "Estate API")
public interface EstateApi {

    @ApiOperation(value = "Save estate", response = Estate.class)
    ResponseEntity<Estate> save(Estate city);
}
