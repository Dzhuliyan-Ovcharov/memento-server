package com.memento.web.endpoint;

import com.memento.model.Permission;
import com.memento.model.User;
import com.memento.service.UserService;
import com.memento.web.converter.UserRegisterRequestToUserPropertyMap;
import com.memento.web.converter.UserToUserRegisterRequestPropertyMap;
import com.memento.web.dto.UserRegisterRequest;
import com.memento.web.endpoint.api.UserApi;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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

    @Autowired
    public UserApiController(final UserService userService,
                             final ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
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
}
