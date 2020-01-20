package com.memento.web.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.memento.MementoStarter;
import com.memento.model.Permission;
import com.memento.model.User;
import com.memento.repository.UserRepository;
import com.memento.service.configuration.BeanConfig;
import com.memento.service.impl.security.JwtTokenUtil;
import com.memento.shared.exception.ResourceNotFoundException;
import io.restassured.RestAssured;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BeanConfig.class, MementoStarter.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-integration-test.properties")
public abstract class BaseApiControllerIntegrationTest {

    static String JWT_TOKEN;

    SoftAssertions softAssertions;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @LocalServerPort
    private Integer port;

    @Before
    public void setUp() {
        softAssertions = new SoftAssertions();
        RestAssured.port = port;
        final User user = userRepository.findAll()
                .stream()
                .filter(u -> u.getRole().getPermission().getValue().equals(Permission.Value.ADMIN))
                .findFirst()
                .orElseThrow(ResourceNotFoundException::new);
        JWT_TOKEN = jwtTokenUtil.generateToken(user, user.getRole().getAuthority());
    }
}
