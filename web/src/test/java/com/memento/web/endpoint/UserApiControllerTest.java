package com.memento.web.endpoint;

import com.memento.model.Permission;
import com.memento.model.Role;
import com.memento.model.User;
import com.memento.web.dto.UserRegisterRequest;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.mail.MailSendException;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Collections;

import static com.memento.web.RequestUrlConstant.USERS_BASE_URL;
import static com.memento.web.constant.JsonPathConstant.USER_REGISTER_REQUEST_COLLECTION_JSON_PATH;
import static com.memento.web.constant.JsonPathConstant.USER_REGISTER_REQUEST_JSON_PATH;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = UserApiController.class)
public class UserApiControllerTest extends BaseApiControllerTest {

    private static final Long ID = 1L;
    private static final String FIRST_NAME = "First name";
    private static final String LAST_NAME = "Last name";
    private static final String EMAIL = "email@email.email";
    private static final String PASSWORD = "password";

    @SpyBean
    private ModelMapper modelMapper;

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifyGetAllUsersAndExpect200() throws Exception {
        final User user = User.builder()
                .id(ID)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .role(Role.builder().id(ID).permission(Permission.BUYER).build())
                .estates(Collections.emptyList())
                .build();

        final String jsonResponse = loadJsonResource(USER_REGISTER_REQUEST_COLLECTION_JSON_PATH, UserRegisterRequest[].class);

        when(userService.getAll()).thenReturn(Collections.singleton(user));

        mockMvc.perform(
                get(USERS_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonResponse, true));

        verify(userService, times(1)).getAll();
        verify(modelMapper, times(1)).map(user, UserRegisterRequest.class);
    }

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifyGetAllUsersWhenUsersAreNotPresentAndExpect200() throws Exception {
        when(userService.getAll()).thenReturn(Collections.emptySet());

        mockMvc.perform(
                get(USERS_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(EMPTY_JSON_COLLECTION, true));

        verify(userService, times(1)).getAll();
        verify(modelMapper, never()).map(any(User.class), eq(UserRegisterRequest.class));
    }

    @Test
    @WithMockUser(authorities = {Permission.Value.AGENCY, Permission.Value.BUYER})
    public void verifyGetAllUsersWhenUserIsNotAuthorizedAndExpect403() throws Exception {
        when(userService.getAll()).thenReturn(Collections.emptySet());

        mockMvc.perform(
                get(USERS_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        verify(userService, never()).getAll();
        verify(modelMapper, never()).map(any(User.class), eq(UserRegisterRequest.class));
    }

    @Test
    public void verifyGetAllUsersWhenUserIsNotAuthenticatedAndExpect401() throws Exception {
        when(userService.getAll()).thenReturn(Collections.emptySet());

        mockMvc.perform(
                get(USERS_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        verify(userService, never()).getAll();
        verify(modelMapper, never()).map(any(User.class), eq(UserRegisterRequest.class));
    }

    @Test
    public void verifyRegisterAndExpect200() throws Exception {
        final String jsonRequest = loadJsonResource(USER_REGISTER_REQUEST_JSON_PATH, UserRegisterRequest.class);

        doNothing().when(userService).register(any(User.class));

        mockMvc.perform(
                post(USERS_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());

        verify(modelMapper, times(1)).map(any(UserRegisterRequest.class), eq(User.class));
        verify(userService, times(1)).register(any(User.class));
    }

    @Test
    public void verifyRegisterWhenEmailExistsAndExpect400() throws Exception {
        final String jsonRequest = loadJsonResource(USER_REGISTER_REQUEST_JSON_PATH, UserRegisterRequest.class);

        doThrow(DuplicateKeyException.class).when(userService).register(any(User.class));

        mockMvc.perform(
                post(USERS_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());

        verify(modelMapper, times(1)).map(any(UserRegisterRequest.class), eq(User.class));
        verify(userService, times(1)).register(any(User.class));
    }

    @Test
    public void verifyRegisterWhenEmailCannotBeSendAndExpect400() throws Exception {
        final String jsonRequest = loadJsonResource(USER_REGISTER_REQUEST_JSON_PATH, UserRegisterRequest.class);

        doThrow(MailSendException.class).when(userService).register(any(User.class));

        mockMvc.perform(
                post(USERS_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());

        verify(modelMapper, times(1)).map(any(UserRegisterRequest.class), eq(User.class));
        verify(userService, times(1)).register(any(User.class));
    }

    @Test
    public void verifyRegisterWhenUserRegisterRequestIsNullAndExpect400() throws Exception {
        mockMvc.perform(
                post(USERS_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(EMPTY_JSON))
                .andExpect(status().isBadRequest());

        verify(modelMapper, never()).map(any(UserRegisterRequest.class), eq(User.class));
        verify(userService, never()).register(any(User.class));
    }
}
