package com.memento.service.impl;

import com.memento.model.EmailVerificationToken;
import com.memento.repository.EmailVerificationRepository;
import com.memento.shared.exception.EmailVerificationTimeExpiryException;
import com.memento.shared.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmailVerificationServiceImplTest {

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
        when(emailVerificationRepository.findByUser_Email(anyString())).thenReturn(Optional.empty());

        boolean isEmailVerified = emailVerificationService.isEmailVerified("");

        assertThat(isEmailVerified, is(equalTo(false)));
        verify(emailVerificationRepository, times(1)).findByUser_Email(anyString());
    }

    @Test
    public void verifyIsEmailVerifiedWhenEmailIsNotVerified() {
        when(token.isEmailVerified()).thenReturn(false);
        when(emailVerificationRepository.findByUser_Email(anyString())).thenReturn(Optional.of(token));

        boolean isEmailVerified = emailVerificationService.isEmailVerified("");

        assertThat(isEmailVerified, is(equalTo(false)));
        verify(emailVerificationRepository, times(1)).findByUser_Email(anyString());
        verify(token, times(1)).isEmailVerified();
    }

    @Test
    public void verifyIsEmailVerifiedWhenTokenIsPresentAndEmailIsVerified() {
        when(token.isEmailVerified()).thenReturn(true);
        when(emailVerificationRepository.findByUser_Email(anyString())).thenReturn(Optional.of(token));

        boolean isEmailVerified = emailVerificationService.isEmailVerified("");

        assertThat(isEmailVerified, is(equalTo(true)));
        verify(emailVerificationRepository, times(1)).findByUser_Email(anyString());
        verify(token, times(1)).isEmailVerified();
    }

    @Test
    public void verifyVerifyEmailWhenTokenIsPresentAndValid() {
        Instant expiryTime = Instant.ofEpochSecond(Instant.now().getEpochSecond() + 10);
        when(emailVerificationRepository.findByToken(anyString())).thenReturn(Optional.of(token));
        when(token.getExpiryTime()).thenReturn(expiryTime);
        when(emailVerificationRepository.save(any(EmailVerificationToken.class))).thenReturn(token);

        emailVerificationService.verifyEmail("");

        verify(emailVerificationRepository, times(1)).findByToken(anyString());
        verify(token, times(2)).getExpiryTime();
        verify(token, times(1)).getId();
        verify(token, times(1)).getUser();
        verify(emailVerificationRepository, times(1)).save(any(EmailVerificationToken.class));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void throwsWhenTokenIsNotPresent() {
        when(emailVerificationRepository.findByToken(anyString())).thenReturn(Optional.empty());

        emailVerificationService.verifyEmail("");

        verify(emailVerificationRepository).findByToken(anyString());
    }

    @Test(expected = EmailVerificationTimeExpiryException.class)
    public void throwsWhenTokenHasExpired() {
        when(emailVerificationRepository.findByToken(anyString())).thenReturn(Optional.of(token));
        when(token.getExpiryTime()).thenReturn(Instant.now());

        emailVerificationService.verifyEmail("");

        verify(emailVerificationRepository).findByToken(anyString());
        verify(token).getExpiryTime();
    }

    @Test
    public void verifySaveWhenTokenIsNotNull() {
        when(emailVerificationRepository.save(any(EmailVerificationToken.class))).thenReturn(token);

        emailVerificationService.save(token);

        verify(emailVerificationRepository).save(any(EmailVerificationToken.class));
    }
}