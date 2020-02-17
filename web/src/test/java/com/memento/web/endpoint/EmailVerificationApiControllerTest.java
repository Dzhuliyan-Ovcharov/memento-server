package com.memento.web.endpoint;

import com.memento.service.EmailVerificationService;
import com.memento.shared.exception.EmailVerificationException;
import com.memento.shared.exception.ResourceNotFoundException;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static com.memento.web.RequestUrlConstant.EMAIL_VERIFICATION_BASE_URL;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EmailVerificationApiController.class)
public class EmailVerificationApiControllerTest extends BaseApiControllerTest {

    private static final String PARAM = "token";
    private static final String TOKEN = "verification token";

    @MockBean
    private EmailVerificationService emailVerificationService;

    @Test
    public void verifyConfirmRegistrationAndExpect200() throws Exception {
        doNothing().when(emailVerificationService).verifyEmail(TOKEN);

        mockMvc.perform(
                get(EMAIL_VERIFICATION_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .param(PARAM, TOKEN))
                .andExpect(status().isOk());

        verify(emailVerificationService, times(1)).verifyEmail(TOKEN);
    }

    @Test
    public void verifyConfirmRegistrationWhenTokenIsNotSendAndExpect400() throws Exception {
        mockMvc.perform(
                get(EMAIL_VERIFICATION_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(emailVerificationService, never()).verifyEmail(anyString());
    }

    @Test
    public void verifyConfirmRegistrationWhenTokenNotFoundAndExpect404() throws Exception {
        doThrow(ResourceNotFoundException.class).when(emailVerificationService).verifyEmail(TOKEN);

        mockMvc.perform(
                get(EMAIL_VERIFICATION_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .param(PARAM, TOKEN))
                .andExpect(status().isNotFound());

        verify(emailVerificationService, times(1)).verifyEmail(TOKEN);
    }

    @Test
    public void verifyConfirmRegistrationWhenTokenHasExpiredAndExpect400() throws Exception {
        doThrow(EmailVerificationException.class).when(emailVerificationService).verifyEmail(TOKEN);

        mockMvc.perform(
                get(EMAIL_VERIFICATION_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .param(PARAM, TOKEN))
                .andExpect(status().isBadRequest());

        verify(emailVerificationService, times(1)).verifyEmail(TOKEN);
    }
}
