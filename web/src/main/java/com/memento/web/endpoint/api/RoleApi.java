package com.memento.web.endpoint.api;

import com.memento.model.Role;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;

import java.util.Set;

@Api(value = "Role API")
public interface RoleApi {

    @ApiOperation(value = "Get all roles", response = Role.class)
    ResponseEntity<Set<Role>> getAll();
}
