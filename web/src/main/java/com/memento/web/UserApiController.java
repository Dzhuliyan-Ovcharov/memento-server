package com.memento.web;

import com.memento.model.User;
import com.memento.service.UserService;
import com.memento.web.api.UserApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/user", produces = { MediaType.APPLICATION_JSON_VALUE })
public class UserApiController implements UserApi {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserApiController(final UserService userService, final BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    @GetMapping(value = "/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAll());
    }

    @Override
    @PostMapping(value = "/register")
    public ResponseEntity<User> register(@Valid @RequestBody final User user) {
        return ResponseEntity.ok(userService.save(user));
    }

    @Override
    @PostMapping(value = "/login")
    public ResponseEntity<User> login(@Valid @RequestBody final User user) {
        final User foundUser = (User) userService.loadUserByUsername(user.getUsername());

        if (!bCryptPasswordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong username or password");
        }

        return ResponseEntity.ok(foundUser);
    }

}
