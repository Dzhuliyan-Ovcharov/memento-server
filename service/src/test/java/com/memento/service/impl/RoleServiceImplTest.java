package java.com.memento.service.impl;

import com.memento.model.Permission;
import com.memento.model.Role;
import com.memento.repository.RoleRepository;
import com.memento.service.impl.RoleServiceImpl;
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
    public void getAllTest() {
        when(roleRepository.findAll()).thenReturn(Collections.emptyList());
        roleService.getAll();
        verify(roleRepository).findAll();
    }

    @Test
    public void saveTest() {
        Role role = mock(Role.class);
        when(roleRepository.save(any(Role.class))).thenReturn(role);
        roleService.save(role);
        verify(roleRepository).save(any(Role.class));
    }

    @Test
    public void saveRolesTest() {
        when(roleRepository.saveAll(anySet())).thenReturn(Collections.emptyList());
        roleService.saveRoles(Collections.emptySet());
        verify(roleRepository).saveAll(anySet());
    }

    @Test
    public void findRoleByPermissionTest() {
        Role role = mock(Role.class);
        when(roleRepository.findRoleByPermission(any(Permission.class))).thenReturn(role);
        roleService.findRoleByPermission(Permission.values()[0]);
        verify(roleRepository).findRoleByPermission(any(Permission.class));
    }
}