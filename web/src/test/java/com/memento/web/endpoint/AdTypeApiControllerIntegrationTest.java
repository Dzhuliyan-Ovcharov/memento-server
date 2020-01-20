package com.memento.web.endpoint;

import com.memento.model.AdType;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Set;

import static com.memento.web.RequestUrlConstant.AD_TYPES_BASE_URL;
import static com.memento.web.constant.JsonPathConstant.AD_TYPE_JSON_PATH;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class AdTypeApiControllerIntegrationTest extends BaseApiControllerIntegrationTest {

    private AdType adType;

    @Before
    public void init() throws IOException {
        adType = objectMapper.readValue(getClass().getResource(AD_TYPE_JSON_PATH), AdType.class);
    }

    @Test
    public void verifySave() {
        final AdType response = given()
                .header("Authorization", "Bearer " + JWT_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(adType)
                .when()
                .post(AD_TYPES_BASE_URL)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .as(AdType.class);

        final AdType expected = adType.toBuilder()
                .id(response.getId())
                .build();

        assertThat(response).isEqualTo(expected);
    }

    @Test
    public void verifyGetAll() {
        final Set<AdType> response = Set.copyOf(
                get(AD_TYPES_BASE_URL)
                        .then()
                        .statusCode(HttpStatus.SC_OK)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .extract()
                        .body()
                        .jsonPath()
                        .get());

        final AdType expected = adType.toBuilder()
                .id(response.iterator().next().getId())
                .build();

        assertThat(response).hasSize(1);
        assertThat(response).contains(expected);
    }
}
