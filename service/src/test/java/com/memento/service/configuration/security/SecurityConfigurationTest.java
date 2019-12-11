package com.memento.service.configuration.security;

import com.memento.service.UserService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@TestConfiguration
@EnableWebMvc
public class SecurityConfigurationTest {

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private UserService userService;

    @MockBean
    private AntPathMatcher antPathMatcher;

    @MockBean
    private MementoAuthenticationProvider authenticationProvider;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean
    private JwtRequestFilter jwtRequestFilter;

    @MockBean
    private AuthenticationSuccessHandler authenticationSuccessHandler;

}
