package com.memento.web.config;

import com.memento.service.EmailVerificationService;
import com.memento.service.UserService;
import com.memento.service.configuration.BeanConfig;
import com.memento.web.security.*;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;

@TestConfiguration
@Import(BeanConfig.class)
public class BeanConfigTest {

    @MockBean
    private UserService userService;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean
    private EmailVerificationService emailVerificationService;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new MementoAuthenticationProvider(userService, emailVerificationService, bCryptPasswordEncoder);
    }

    @Bean
    public JwtTokenUtil jwtTokenUtil() {
        return new JwtTokenUtil();
    }

    @Bean
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }

    @Bean
    public AuthWhiteListHelper authWhiteListHelper() {
        return new AuthWhiteListHelper(new HashSet<>());
    }

    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter(jwtTokenUtil(), userService, authWhiteListHelper());
    }
}
