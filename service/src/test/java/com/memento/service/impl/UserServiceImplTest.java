package com.memento.service.impl;

import com.memento.model.EmailVerificationToken;
import com.memento.model.Permission;
import com.memento.model.Role;
import com.memento.model.User;
import com.memento.repository.UserRepository;
import com.memento.service.EmailService;
import com.memento.service.EmailVerificationService;
import com.memento.service.RoleService;
import com.memento.shared.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleService roleService;

    @Mock
    private EmailService emailService;

    @Mock
    private EmailVerificationService emailVerificationService;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @Before
    public void setUp() {
        user = mock(User.class);
    }

    @Test
    public void verifyGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        userService.getAll();

        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void verifyRegisterWhenUserIsNew() {
        Role role = mock(Role.class);
        User savedUser = mock(User.class);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(user.getEmail()).thenReturn("");
        when(user.getRole()).thenReturn(role);
        when(role.getPermission()).thenReturn(Permission.BUYER);
        when(roleService.findRoleByPermission(any(Permission.class))).thenReturn(role);
        when(user.getPassword()).thenReturn("");
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        doNothing().when(emailService).sendMail(anyString(), anyString());
        doNothing().when(emailVerificationService).save(any(EmailVerificationToken.class));

        userService.register(user);

        verify(userRepository, times(1)).findByEmail(anyString());
        verify(user, times(2)).getEmail();
        verify(user, times(1)).getFirstName();
        verify(user, times(1)).getLastName();
        verify(roleService, times(1)).findRoleByPermission(any(Permission.class));
        verify(user, times(1)).getRole();
        verify(role, times(1)).getPermission();
        verify(bCryptPasswordEncoder, times(1)).encode(anyString());
        verify(user, times(1)).getPassword();
        verify(userRepository, times(1)).save(any(User.class));
        verify(emailService, times(1)).sendMail(anyString(), anyString());
        verify(emailVerificationService, times(1)).save(any(EmailVerificationToken.class));
    }

    @Test(expected = NullPointerException.class)
    public void throwsWhenUserIsNull() {
        userService.register(null);
    }

    @Test(expected = DuplicateKeyException.class)
    public void throwsWhenUserEmailExists() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(user.getEmail()).thenReturn("");

        userService.register(user);

        verify(userRepository, times(1)).findByEmail(anyString());
        verify(user, times(1)).getEmail();
    }

    @Test
    public void verifyUpdateWhenUserIsNotNull() {
        User oldUser = mock(User.class);
        User newUser = mock(User.class);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(oldUser));
        when(user.getPassword()).thenReturn("");
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("");
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        userService.update(user);

        verify(userRepository, times(1)).findById(anyLong());
        verify(user, times(1)).getId();
        verify(oldUser, times(1)).getId();
        verify(user, times(1)).getFirstName();
        verify(user, times(1)).getLastName();
        verify(user, times(1)).getPassword();
        verify(bCryptPasswordEncoder, times(1)).encode(anyString());
        verify(user, times(1)).getRole();
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test(expected = NullPointerException.class)
    public void verifyUpdateThrowsWhenUserIsNull() {
        userService.update(null);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void throwsWhenUserIsNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        userService.update(user);

        verify(userRepository, times(1)).findById(anyLong());
        verify(user, times(1)).getId();
    }

    @Test
    public void verifyFindByEmailWhenUserEmailIsFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        userService.findByEmail("");

        verify(userRepository, times(1)).findByEmail(anyString());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void throwsWhenUserEmailIsNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        userService.findByEmail("");

        verify(userRepository, times(1)).findByEmail(anyString());
    }


    @Test
    public void verifyFindByIdWhenIdIsValid() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        userService.findById(1L);

        verify(userRepository).findById(anyLong());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void throwsWhenIdIsNotValid() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        userService.findById(0L);

        verify(userRepository).findById(anyLong());
    }

    @Test
    public void verifyLoadUserByUsernameWhenEmailIsValid() {
        Role role = mock(Role.class);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(user.getEmail()).thenReturn("email");
        when(user.getPassword()).thenReturn("");
        when(user.getRole()).thenReturn(role);

        userService.loadUserByUsername("");

        verify(userRepository).findByEmail(anyString());
        verify(user, times(1)).getEmail();
        verify(user, times(1)).getPassword();
        verify(user, times(1)).getRole();
    }

    @Test(expected = ResourceNotFoundException.class)
    public void throwsWhenUserEmailIsNotPresent() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        userService.loadUserByUsername("");

        verify(userRepository).findByEmail(anyString());
    }

    @Test(expected = NullPointerException.class)
    public void throwsWhenEmailIsNull() {
        userService.loadUserByUsername(null);
    }
}