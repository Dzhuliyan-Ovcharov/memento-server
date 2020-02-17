package com.memento.web.endpoint.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.memento.MementoStarter;
import com.memento.model.Permission;
import com.memento.model.User;
import com.memento.repository.UserRepository;
import com.memento.service.configuration.BeanConfig;
import com.memento.web.security.JwtTokenUtil;
import com.memento.shared.exception.ResourceNotFoundException;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.RandomStringUtils;
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
        }
    }

    <T> T getResource(final String requestURI, final Class<T> responseClass) {
        return getResource(requestURI, HttpStatus.SC_OK)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                    .body()
                    .as(responseClass);
    }

    ValidatableResponse getResource(final String requestURI, final Integer expectedStatusCode) {
        return getDefaultRequestSpecification()
                .when()
                    .get(requestURI)
                .then()
                    .statusCode(expectedStatusCode);
    }

    <T> List<T> getAll(final String requestURI, final Class<T> responseClass) {
        return getResource(requestURI, HttpStatus.SC_OK)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                    .body()
                    .jsonPath()
                    .getList(".", responseClass);
    }

    <T> T saveResource(final T resource, final String requestURI, final Class<T> responseClass) {
        return saveResource(resource, requestURI, HttpStatus.SC_OK)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                    .body()
                    .as(responseClass);
    }

    <T> ValidatableResponse saveResource(final T resource, final String requestURI, final Integer expectedStatusCode) {
        return getDefaultRequestSpecification()
                    .body(resource)
                .when()
                    .post(requestURI)
                .then()
                    .statusCode(expectedStatusCode);
    }

    <T> T updateResource(final T resource, final String requestURI, final Class<T> responseClass) {
        return updateResource(resource, requestURI, HttpStatus.SC_OK)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                    .body()
                    .as(responseClass);
    }

    <T> ValidatableResponse updateResource(final T resource, final String requestURI, final Integer expectedStatusCode) {
        return getDefaultRequestSpecification()
                    .body(resource)
                .when()
                    .put(requestURI)
                .then()
                    .statusCode(expectedStatusCode);
    }

    String generateRandomString() {
        return RandomStringUtils.randomAlphabetic(30);
    }

    private RequestSpecification getDefaultRequestSpecification() {
        return given()
                .header("Authorization", "Bearer " + JWT_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE);
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
