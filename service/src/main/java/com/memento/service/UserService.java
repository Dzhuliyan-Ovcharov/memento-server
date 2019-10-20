package com.memento.service;

import com.memento.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Set;

public interface UserService extends UserDetailsService {

    Set<User> getAll();

    Set<User> getAllByUsernames(Set<String> usernames);

    User findById(Long id);

    User register(User user);

    User update(User user);
}
