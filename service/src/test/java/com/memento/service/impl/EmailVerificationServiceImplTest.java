package com.memento.service.impl;

import com.memento.model.EmailVerificationToken;
import com.memento.repository.EmailVerificationRepository;
import com.memento.shared.exception.EmailVerificationException;
import com.memento.shared.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmailVerificationServiceImplTest {

    private static final String EMAIL = "email";
    private static final String TOKEN = "token";

    @Mock
    private EmailVerificationRepository emailVerificationRepository;

    @InjectMocks
    private EmailVerificationServiceImpl emailVerificationService;

    private EmailVerificationToken token;

    @Before
    public void setUp() {
        token = mock(EmailVerificationToken.class);
    }

    @Test
    public void verifyIsEmailVerifiedWhenTokenIsNotPresent() {
        when(emailVerificationRepository.findByUser_Email(EMAIL)).thenReturn(Optional.empty());

        boolean isEmailVerified = emailVerificationService.isEmailVerified(EMAIL);

        assertFalse(isEmailVerified);
        verify(emailVerificationRepository, times(1)).findByUser_Email(EMAIL);
    }

    @Test
    public void verifyIsEmailVerifiedWhenEmailIsNotVerified() {
        when(token.isEmailVerified()).thenReturn(false);
        when(emailVerificationRepository.findByUser_Email(EMAIL)).thenReturn(Optional.of(token));

        boolean isEmailVerified = emailVerificationService.isEmailVerified(EMAIL);

        assertFalse(isEmailVerified);
        verify(emailVerificationRepository, times(1)).findByUser_Email(EMAIL);
        verify(token, times(1)).isEmailVerified();
    }

    @Test
    public void verifyIsEmailVerifiedWhenTokenIsPresentAndEmailIsVerified() {
        when(token.isEmailVerified()).thenReturn(true);
        when(emailVerificationRepository.findByUser_Email(EMAIL)).thenReturn(Optional.of(token));

        boolean isEmailVerified = emailVerificationService.isEmailVerified(EMAIL);

        assertTrue(isEmailVerified);
        verify(emailVerificationRepository, times(1)).findByUser_Email(EMAIL);
        verify(token, times(1)).isEmailVerified();
    }

    @Test(expected = NullPointerException.class)
    public void verifyIsEmailVerifiedThrowsWhenEmailIsNull() {
        emailVerificationService.verifyEmail(null);
    }

    @Test
    public void verifyVerifyEmailWhenTokenIsPresentAndValid() {
        Instant expiryTime = Instant.ofEpochSecond(Instant.now().getEpochSecond() + 10);
        when(emailVerificationRepository.findByToken(TOKEN)).thenReturn(Optional.of(token));
        when(token.getExpiryTime()).thenReturn(expiryTime);
        when(emailVerificationRepository.save(any(EmailVerificationToken.class))).thenReturn(token);

        emailVerificationService.verifyEmail(TOKEN);

        verify(emailVerificationRepository, times(1)).findByToken(TOKEN);
        verify(token, times(2)).getExpiryTime();
        verify(token, times(1)).getId();
        verify(token, times(1)).getUser();
        verify(emailVerificationRepository, times(1)).save(any(EmailVerificationToken.class));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void verifyVerifyEmailThrowsWhenTokenIsNotPresent() {
        when(emailVerificationRepository.findByToken(anyString())).thenReturn(Optional.empty());

        emailVerificationService.verifyEmail(TOKEN);

        verify(emailVerificationRepository, times(1)).findByToken(TOKEN);
    }

    @Test(expected = EmailVerificationException.class)
    public void verifyVerifyEmailThrowsWhenTokenHasExpired() {
        when(emailVerificationRepository.findByToken(TOKEN)).thenReturn(Optional.of(token));
        when(token.getExpiryTime()).thenReturn(Instant.now());

        emailVerificationService.verifyEmail(TOKEN);

        verify(emailVerificationRepository, times(1)).findByToken(TOKEN);
        verify(token, times(1)).getExpiryTime();
    }

    @Test(expected = NullPointerException.class)
    public void verifyVerifyEmailThrowsWhenTokenIsNull() {
        emailVerificationService.verifyEmail(null);
    }

    @Test
    public void verifySave() {
        when(emailVerificationRepository.save(token)).thenReturn(token);

        emailVerificationService.save(token);

        verify(emailVerificationRepository, times(1)).save(token);
    }

    @Test(expected = NullPointerException.class)
    public void verifySaveWhenTokenIsNull() {
        emailVerificationService.save(null);
    }
}
