package com.memento.web.endpoint.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.memento.MementoStarter;
import com.memento.model.Permission;
import com.memento.model.User;
import com.memento.repository.UserRepository;
import com.memento.service.configuration.BeanConfig;
import com.memento.service.impl.security.JwtTokenUtil;
import com.memento.shared.exception.ResourceNotFoundException;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BeanConfig.class, MementoStarter.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = "classpath:application-integration-test.properties")
public abstract class BaseApiControllerIntTest {

    private static String JWT_TOKEN;

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
        if (JWT_TOKEN == null) {
            JWT_TOKEN = generateJwtToken();
            RestAssured.requestSpecification = new RequestSpecBuilder()
                    .addHeader("Authorization", "Bearer " + JWT_TOKEN)
                    .build();
        }
    }

    <T> T getResource(final String requestURI, final Class<T> resourceClass) {
        return get(requestURI)
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                    .body()
                    .as(resourceClass);
    }

    <T> List<T> getAll(final String requestURI, final Class<T> resourceClass) {
        return get(requestURI)
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                    .body()
                    .jsonPath()
                    .getList(".", resourceClass);
    }

    <T> T saveResource(final String requestURI, final T resource, final Class<T> resourceClass) {
        return given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(resource)
                .when()
                    .post(requestURI)
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                    .body()
                    .as(resourceClass);
    }

    <T> T updateResource(final String requestURI, final T resource, final Class<T> resourceClass) {
        return given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(resource)
                .when()
                    .put(requestURI)
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                    .body()
                    .as(resourceClass);
    }

    private String generateJwtToken() {
        final User user = userRepository.findAll()
                .stream()
                .filter(u -> u.getRole().getPermission().getValue().equals(Permission.Value.ADMIN))
                .findFirst()
                .orElseThrow(ResourceNotFoundException::new);
        return jwtTokenUtil.generateToken(user, user.getRole().getAuthority());
    }
}