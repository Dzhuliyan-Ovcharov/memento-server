package com.memento.web.endpoint;

import com.memento.service.EmailVerificationService;
import com.memento.shared.exception.EmailVerificationTimeExpiryException;
import com.memento.shared.exception.ResourceNotFoundException;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EmailVerificationApiController.class)
public class EmailVerificationApiControllerTest extends BaseApiControllerTest {

    @MockBean
    private EmailVerificationService emailVerificationService;

    @Test
    public void verifyConfirmRegistrationAndExpect200() throws Exception {
        doNothing().when(emailVerificationService).verifyEmail(anyString());

        mockMvc.perform(
                get("/api/email/verify")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("token", "token"))
                .andExpect(status().isOk());

        verify(emailVerificationService, times(1)).verifyEmail(anyString());
    }

    @Test
    public void verifyConfirmRegistrationWhenTokenIsNotSendAndExpect400() throws Exception {
        doNothing().when(emailVerificationService).verifyEmail(anyString());

        mockMvc.perform(
                get("/api/email/verify")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(emailVerificationService, never()).verifyEmail(anyString());
    }

    @Test
    public void verifyConfirmRegistrationWhenTokenNotFoundAndExpect404() throws Exception {
        doThrow(ResourceNotFoundException.class).when(emailVerificationService).verifyEmail(anyString());

        mockMvc.perform(
                get("/api/email/verify")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("token", "token"))
                .andExpect(status().isNotFound());

        verify(emailVerificationService, times(1)).verifyEmail(anyString());
    }

    @Test
    public void verifyConfirmRegistrationWhenTokenHasExpiredAndExpect400() throws Exception {
        doThrow(EmailVerificationTimeExpiryException.class).when(emailVerificationService).verifyEmail(anyString());

        mockMvc.perform(
                get("/api/email/verify")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("token", "token"))
                .andExpect(status().isBadRequest());

        verify(emailVerificationService, times(1)).verifyEmail(anyString());
    }
}
