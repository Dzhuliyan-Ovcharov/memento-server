package com.memento.web.endpoint.api;

import com.memento.web.view.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

@Api(value = "User API")
public interface UserApi {

    @ApiOperation(value = "Get all users", response = Set.class)
    ResponseEntity<Set<UserDTO>> getAllUsers();

    @ApiOperation(value = "Register new user", response = UserDTO.class)
    ResponseEntity<UserDTO> register(UserDTO userDTO);

    @ApiOperation(value = "Login", response = UserDTO.class)
    ResponseEntity<UserDTO> login(UserDTO userDTO);
}
