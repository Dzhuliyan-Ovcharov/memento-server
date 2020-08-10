package com.memento.repository;

import com.memento.model.Permission;
import com.memento.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findRoleByPermission(Permission permission);
}
