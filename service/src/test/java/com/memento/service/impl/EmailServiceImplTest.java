package com.memento.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceImplTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    @Test
    public void verifySendMailWhenEmailToIsValid() {
        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

        emailService.sendMail("", "");

        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test(expected = MailSendException.class)
    public void throwWhenFailToParseTheMessage() {
        doThrow(MailParseException.class).when(javaMailSender).send(any(SimpleMailMessage.class));

        emailService.sendMail("", "");

        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test(expected = MailSendException.class)
    public void throwsWhenFailToAuthenticate() {
        doThrow(MailAuthenticationException.class).when(javaMailSender).send(any(SimpleMailMessage.class));

        emailService.sendMail("", "");

        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test(expected = MailSendException.class)
    public void throwsWhenMailCannotBeSend() {
        doThrow(MailSendException.class).when(javaMailSender).send(any(SimpleMailMessage.class));

        emailService.sendMail("", "");

        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }


}