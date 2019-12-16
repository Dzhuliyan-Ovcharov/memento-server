package com.memento.web.endpoint;

import com.memento.model.Permission;
import com.memento.model.Role;
import com.memento.model.User;
import com.memento.web.dto.UserRegisterRequest;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Collections;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = UserApiController.class)
public class UserApiControllerTest extends BaseApiControllerTest {

    @SpyBean
    private ModelMapper modelMapper;

    private User user;

    private UserRegisterRequest userRegisterRequest;

    @Before
    public void init() {
        user = User.builder()
                .id(1L)
                .firstName("First name")
                .lastName("Last name")
                .email("email@email.email")
                .password("password")
                .role(Role.builder().id(2L).permission(Permission.BUYER).build())
                .estates(Collections.emptyList())
                .build();

        userRegisterRequest = UserRegisterRequest.builder()
                .firstName("First name")
                .lastName("Last name")
                .email("email@email.email")
                .permission(Permission.BUYER)
                .password("password")
                .confirmPassword("password")
                .build();
    }

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifyGetAllUsersAndExpect200() throws Exception {
        final Set<User> users = Collections.singleton(user);

        when(userService.getAll()).thenReturn(users);

        mockMvc.perform(
                get("/api/user/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].*", hasSize(6)))
                .andExpect(jsonPath("$.[0].firstName", is("First name")))
                .andExpect(jsonPath("$.[0].lastName", is("Last name")))
                .andExpect(jsonPath("$.[0].email", is("email@email.email")))
                .andExpect(jsonPath("$.[0].password", is("password")))
                .andExpect(jsonPath("$.[0].confirmPassword", is(nullValue())));
        //Todo : verify "$.[0].permission" (modelMapper is not mapping the role)

        verify(userService, times(1)).getAll();
        verify(modelMapper, times(1)).map(any(Object.class), any(Class.class));
    }

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifyGetAllUsersWhenUsersAreNotPresentAndExpect200() throws Exception {
        when(userService.getAll()).thenReturn(Collections.emptySet());

        mockMvc.perform(
                get("/api/user/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(empty())));

        verify(userService, times(1)).getAll();
        verify(modelMapper, never()).map(any(Object.class), any(Class.class));
    }

    @Test
    @WithMockUser(authorities = {Permission.Value.AGENCY, Permission.Value.BUYER})
    public void verifyGetAllUsersWhenUserIsNotAuthorizedAndExpect403() throws Exception {
        when(userService.getAll()).thenReturn(Collections.emptySet());

        mockMvc.perform(
                get("/api/user/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        verify(userService, never()).getAll();
        verify(modelMapper, never()).map(any(Object.class), any(Class.class));
    }

    @Test
    public void verifyGetAllUsersWhenUserIsNotAuthenticatedAndExpect401() throws Exception {
        when(userService.getAll()).thenReturn(Collections.emptySet());

        mockMvc.perform(
                get("/api/user/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        verify(userService, never()).getAll();
        verify(modelMapper, never()).map(any(Object.class), any(Class.class));
    }

    @Test
    public void verifyRegisterAndExpect200() throws Exception {
        final String jsonString = objectMapper.writeValueAsString(userRegisterRequest);

        doNothing().when(userService).register(any(User.class));

        mockMvc.perform(
                post("/api/user/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isOk());

        verify(modelMapper, times(1)).map(any(Object.class), any(Class.class));
        verify(userService, times(1)).register(any(User.class));
    }

    @Test
    public void verifyRegisterWhenEmailExistsAndExpect400() throws Exception {
        final String jsonString = objectMapper.writeValueAsString(userRegisterRequest);

        doThrow(DuplicateKeyException.class).when(userService).register(any(User.class));

        mockMvc.perform(
                post("/api/user/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isBadRequest());

        verify(modelMapper, times(1)).map(any(Object.class), any(Class.class));
        verify(userService, times(1)).register(any(User.class));
    }

    @Test
    public void verifyRegisterWhenUserRegisterRequestIsNullAndExpect400() throws Exception {
        final String jsonString = objectMapper.writeValueAsString(null);

        mockMvc.perform(
                post("/api/user/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isBadRequest());

        verify(modelMapper, never()).map(any(Object.class), any(Class.class));
        verify(userService, never()).register(any(User.class));
    }

}