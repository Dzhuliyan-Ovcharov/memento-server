package com.memento.web.endpoint;

import com.memento.model.User;
import com.memento.service.UserService;
import com.memento.web.endpoint.api.UserApi;
import com.memento.web.view.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/user", produces = {MediaType.APPLICATION_JSON_VALUE})
public class UserApiController implements UserApi {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserApiController(final UserService userService,
                             final ModelMapper modelMapper,
                             final BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    @GetMapping(value = "/all")
    public ResponseEntity<Set<UserDTO>> getAllUsers() {
        Set<UserDTO> response = userService.getAll().stream().map(u -> modelMapper.map(u, UserDTO.class)).collect(Collectors.toSet());
        return ResponseEntity.ok(response);
    }

    @Override
    @PostMapping(value = "/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody final UserDTO userDTO) {
        final User user = modelMapper.map(userDTO, User.class);
        final UserDTO response = modelMapper.map(userService.register(user), UserDTO.class);
        return ResponseEntity.ok(response);
    }

    @Override
    @PostMapping(value = "/login")
    public ResponseEntity<UserDTO> login(@Valid @RequestBody final UserDTO userDTO) {
        final User user = (User) userService.loadUserByUsername(userDTO.getUsername());

        if (!bCryptPasswordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong username or password");
        }

        final UserDTO response = modelMapper.map(user, UserDTO.class);
        return ResponseEntity.ok(response);
    }

}
