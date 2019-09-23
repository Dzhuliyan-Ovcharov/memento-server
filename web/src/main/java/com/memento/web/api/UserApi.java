package com.memento.web.api;

import com.memento.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Api(value = "User API")
public interface UserApi {

    @ApiOperation(value = "Get all users", response = List.class)
    ResponseEntity<List<User>> getAllUsers();

    @ApiOperation(value = "Register new user", response = User.class)
    ResponseEntity<User> register(User user);

    @ApiOperation(value = "Login", response = User.class)
    ResponseEntity<User> login(User user);
}
