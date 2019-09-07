package com.memento.service;

import com.memento.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> getAll();

    User findById(final Long id);

    User save(final User user);

    User update(final User user);
}
