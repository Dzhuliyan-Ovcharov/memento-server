package com.memento.web.endpoint.api;

import com.memento.web.dto.TokenResponse;
import com.memento.web.dto.UserAuthenticateRequest;
import com.memento.web.dto.UserRegisterRequest;
import com.memento.web.dto.user.UserProfileResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.Set;

@Api(value = "User API")
public interface UserApi {

    @ApiOperation(value = "Get all users", response = Set.class)
    ResponseEntity<Set<UserRegisterRequest>> getAllUsers();

    @ApiOperation(value = "Register new user", response = HttpStatus.class)
    ResponseEntity<HttpStatus> register(UserRegisterRequest userRegisterRequest);

    @ApiOperation(value = "Return user profile", response = UserProfileResponse.class)
    ResponseEntity<UserProfileResponse> getUserProfile(String email);

    @ApiOperation(value = "Return the token", response = TokenResponse.class)
    ResponseEntity<TokenResponse> authenticate(@Valid @RequestBody final UserAuthenticateRequest userAuthenticateRequest);
}
