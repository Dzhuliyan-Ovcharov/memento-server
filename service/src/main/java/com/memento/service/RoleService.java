package com.memento.service;

import com.memento.model.Role;

import java.util.List;
import java.util.Set;

public interface RoleService {

    Set<Role> getAll();

    Role save(Role role);

}
