package com.memento.service.impl;

import com.memento.model.Role;
import com.memento.repository.RoleRepository;
import com.memento.service.RoleService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Primary
@Log4j2
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(final RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Set<Role> getAll() {
        return Set.copyOf(roleRepository.findAll());
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }
}
