package com.memento.service;

import com.memento.model.Role;

import java.util.List;

public interface RoleService {

    List<Role> getAll();

    Role save(final Role role);

}
