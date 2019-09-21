package com.memento.service;

import com.memento.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> getAll();

    User findById(Long id);

    User save(User user);

    User update(User user);
}
