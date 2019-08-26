package com.memento.service;

import com.memento.model.RoleName;
import com.memento.model.User;
import com.memento.repository.RoleRepository;
import com.memento.repository.UserRepository;
import com.memento.shared.exception.ResourceNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Primary
@Log4j2
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(final UserRepository userRepository,
                           final RoleRepository roleRepository,
                           final BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User save(final User user) {
        Objects.requireNonNull(user, "User cannot be null.");
        final User newUser = User.builder()
                .username(user.getUsername())
                .role(roleRepository.findRoleByRoleName(RoleName.CLIENT))
                .password(bCryptPasswordEncoder.encode(user.getPassword()))
                .build();
        return userRepository.save(newUser);
    }

    @Override
    public User findById(final Long id) {
        Objects.requireNonNull(id, "id cannot be null.");
        log.info("Search for user with id: " + id);
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cannot find user with id: " + id));
    }

    @Override
    public UserDetails loadUserByUsername(final String username) {
        Objects.requireNonNull(username, "Username cannot be null.");
        log.info("Loading user with username: " + username);
        return userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Cannot find User with username:" + username));
    }
}
