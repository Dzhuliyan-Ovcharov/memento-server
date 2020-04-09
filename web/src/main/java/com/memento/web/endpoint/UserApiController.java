package com.memento.web.endpoint;

import com.memento.model.Permission;
import com.memento.model.User;
import com.memento.service.UserService;
import com.memento.web.converter.UserRegisterRequestToUserPropertyMap;
import com.memento.web.converter.UserToUserRegisterRequestPropertyMap;
import com.memento.web.dto.TokenResponse;
import com.memento.web.dto.UserAuthenticateRequest;
import com.memento.web.dto.UserRegisterRequest;
import com.memento.web.dto.user.UserProfileResponse;
import com.memento.web.endpoint.api.UserApi;
import com.memento.web.security.JwtTokenUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

import static com.memento.web.RequestUrlConstant.USERS_BASE_URL;

@RestController
@RequestMapping(value = USERS_BASE_URL, produces = {MediaType.APPLICATION_JSON_VALUE})
public class UserApiController implements UserApi {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public UserApiController(final UserService userService,
                             final AuthenticationManager authenticationManager,
                             final ModelMapper modelMapper,
                             final JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.modelMapper = modelMapper;
        this.jwtTokenUtil = jwtTokenUtil;
        modelMapper.addMappings(new UserToUserRegisterRequestPropertyMap());
        modelMapper.addMappings(new UserRegisterRequestToUserPropertyMap());
    }

    @Override
    @Secured(Permission.Value.ADMIN)
    @GetMapping
    public ResponseEntity<Set<UserRegisterRequest>> getAllUsers() {
        return ResponseEntity.ok(userService.getAll()
                .stream()
                .map(user -> modelMapper.map(user, UserRegisterRequest.class))
                .collect(Collectors.toSet()));
    }

    @Override
    @PostMapping
    public ResponseEntity<HttpStatus> register(@Valid @RequestBody final UserRegisterRequest userRegisterRequest) {
        final User user = modelMapper.map(userRegisterRequest, User.class);
        userService.register(user);
        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping(value = "/{email}")
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable final String email) {
        final User user = userService.findByEmail(email);
        final UserProfileResponse userProfileResponse = modelMapper.map(user, UserProfileResponse.class);
        return ResponseEntity.ok(userProfileResponse);
    }

    @Override
    @PostMapping(value = "/authenticate")
    public ResponseEntity<TokenResponse> authenticate(@Valid @RequestBody final UserAuthenticateRequest userAuthenticateRequest) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userAuthenticateRequest.getEmail(), userAuthenticateRequest.getPassword()));
        final User user = (User) authentication.getPrincipal();
        final String token = jwtTokenUtil.generateToken(user, user.getRole().getAuthority());
        return ResponseEntity.ok(TokenResponse.builder().token(token).build());
    }
}
