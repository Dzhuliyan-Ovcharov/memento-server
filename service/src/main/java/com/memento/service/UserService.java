package com.memento.service;

import com.memento.model.User;

import java.util.Set;

public interface UserService {

    Set<User> getAll();

    User findById(Long id);

    void register(User user);

    User update(User user);

    User findByEmail(String email);
}
