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
import org.apache.commons.lang3.StringUtils;
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

    private static final Long ID = 1L;
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";

    private User user;

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
    public void verifyRegister() {
        final Role role = mock(Role.class);
        final User savedUser = mock(User.class);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(user.getEmail()).thenReturn(EMAIL);
        when(user.getRole()).thenReturn(role);
        when(role.getPermission()).thenReturn(Permission.BUYER);
        when(roleService.findRoleByPermission(any(Permission.class))).thenReturn(role);
        when(user.getPassword()).thenReturn(PASSWORD);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(StringUtils.EMPTY);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        doNothing().when(emailService).sendMail(anyString(), anyString());
        doNothing().when(emailVerificationService).save(any(EmailVerificationToken.class));

        userService.register(user);

        verify(userRepository, times(1)).findByEmail(EMAIL);
        verify(user, times(2)).getEmail();
        verify(user, times(1)).getFirstName();
        verify(user, times(1)).getLastName();
        verify(roleService, times(1)).findRoleByPermission(Permission.BUYER);
        verify(user, times(1)).getRole();
        verify(role, times(1)).getPermission();
        verify(bCryptPasswordEncoder, times(1)).encode(PASSWORD);
        verify(user, times(1)).getPassword();
        verify(userRepository, times(1)).save(any(User.class));
        verify(emailService, times(1)).sendMail(eq(EMAIL), anyString());
        verify(emailVerificationService, times(1)).save(any(EmailVerificationToken.class));
    }

    @Test(expected = NullPointerException.class)
    public void verifyRegisterThrowsWhenUserIsNull() {
        userService.register(null);
    }

    @Test(expected = DuplicateKeyException.class)
    public void verifyRegisterThrowsWhenUserEmailExists() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(user.getEmail()).thenReturn(EMAIL);

        userService.register(user);

        verify(userRepository, times(1)).findByEmail(EMAIL);
        verify(user, times(1)).getEmail();
    }

    @Test
    public void verifyUpdate() {
        final User oldUser = mock(User.class);
        final User newUser = mock(User.class);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(oldUser));
        when(user.getPassword()).thenReturn(PASSWORD);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(StringUtils.EMPTY);
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        userService.update(user);

        verify(userRepository, times(1)).findById(anyLong());
        verify(user, times(1)).getId();
        verify(oldUser, times(1)).getId();
        verify(user, times(1)).getFirstName();
        verify(user, times(1)).getLastName();
        verify(user, times(1)).getPassword();
        verify(bCryptPasswordEncoder, times(1)).encode(PASSWORD);
        verify(user, times(1)).getRole();
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test(expected = NullPointerException.class)
    public void verifyUpdateThrowsWhenUserIsNull() {
        userService.update(null);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void verifyUpdateThrowsWhenUserIsNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        userService.update(user);

        verify(userRepository, times(1)).findById(anyLong());
        verify(user, times(1)).getId();
    }

    @Test
    public void verifyFindByEmail() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        userService.findByEmail(EMAIL);

        verify(userRepository, times(1)).findByEmail(EMAIL);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void verifyFindByEmailThrowsWhenUserEmailIsNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        userService.findByEmail(EMAIL);

        verify(userRepository, times(1)).findByEmail(EMAIL);
    }

    @Test(expected = NullPointerException.class)
    public void verifyFindByEmailThrowsWhenUserEmailIsNull() {
        userService.findByEmail(null);
    }

    @Test
    public void verifyFindById() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        userService.findById(ID);

        verify(userRepository, times(1)).findById(ID);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void verifyFindByIdThrowsWhenIdIsNotValid() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        userService.findById(ID);

        verify(userRepository, times(1)).findById(ID);
    }

    @Test(expected = NullPointerException.class)
    public void verifyFindByIdThrowsWhenIdIsNull() {
        userService.findById(null);
    }

    @Test
    public void verifyLoadUserByUsername() {
        final Role role = mock(Role.class);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(user.getEmail()).thenReturn(EMAIL);
        when(user.getPassword()).thenReturn(PASSWORD);
        when(user.getRole()).thenReturn(role);

        userService.loadUserByUsername(EMAIL);

        verify(userRepository, times(1)).findByEmail(EMAIL);
        verify(user, times(1)).getEmail();
        verify(user, times(1)).getPassword();
        verify(user, times(1)).getRole();
    }

    @Test(expected = ResourceNotFoundException.class)
    public void verifyLoadUserByUsernameThrowsWhenUserEmailIsNotPresent() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        userService.loadUserByUsername(EMAIL);

        verify(userRepository, times(1)).findByEmail(EMAIL);
    }

    @Test(expected = NullPointerException.class)
    public void verifyLoadUserByUsernameThrowsWhenEmailIsNull() {
        userService.loadUserByUsername(null);
    }
}
