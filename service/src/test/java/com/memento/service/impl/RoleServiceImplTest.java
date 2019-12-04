package com.memento.service.impl;

import com.memento.model.Permission;
import com.memento.model.Role;
import com.memento.repository.RoleRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    public void getAll_whenRolesAreAvailable_expectTheRoles() {
        when(roleRepository.findAll()).thenReturn(Collections.emptyList());

        roleService.getAll();

        verify(roleRepository, times(1)).findAll();
    }

    @Test
    public void save_whenRoleIsNotNull_expectToSave() {
        Role role = mock(Role.class);
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        roleService.save(role);

        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    public void saveRoles_whenRolesAreNotNull_expectToSave() {
        when(roleRepository.saveAll(anySet())).thenReturn(Collections.emptyList());

        roleService.saveRoles(Collections.emptySet());

        verify(roleRepository, times(1)).saveAll(anySet());
    }

    @Test
    public void findRoleByPermission_whenRolesAreFound_expectTheRoles() {
        Role role = mock(Role.class);
        when(roleRepository.findRoleByPermission(any(Permission.class))).thenReturn(role);

        roleService.findRoleByPermission(Permission.BUYER);

        verify(roleRepository, times(1)).findRoleByPermission(any(Permission.class));
    }
}