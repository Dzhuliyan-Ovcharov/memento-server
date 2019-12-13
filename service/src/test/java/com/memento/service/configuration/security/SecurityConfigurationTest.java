package com.memento.service.configuration.security;

import com.memento.service.UserService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;

@TestConfiguration
@ComponentScan(basePackages = "com.memento.service.configuration.security")
public class SecurityConfigurationTest {

    @MockBean
    private UserService userService;

}
