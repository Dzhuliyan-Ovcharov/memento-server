package com.memento.web.endpoint.integration;

import com.memento.model.AdType;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Set;

import static com.memento.web.RequestUrlConstant.AD_TYPES_BASE_URL;
import static com.memento.web.constant.JsonPathConstant.AD_TYPE2_JSON_PATH;
import static com.memento.web.constant.JsonPathConstant.AD_TYPE_JSON_PATH;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class AdTypeApiControllerIntTest extends BaseApiControllerIntTest {

    @Test
    public void crudHappyAdType() throws IOException {
        final AdType toSave = objectMapper.readValue(getClass().getResource(AD_TYPE_JSON_PATH), AdType.class);
        final AdType savedAdType = saveResource(AD_TYPES_BASE_URL, toSave, AdType.class);
        final AdType expectedToSave = toSave.toBuilder()
                .id(savedAdType.getId())
                .build();

        final Set<AdType> resultAfterSave = Set.copyOf(getAll(AD_TYPES_BASE_URL, AdType.class));
        assertThat(resultAfterSave).contains(expectedToSave);
    }

    @Test
    public void saveDuplicateAdTypeAndExpect400() throws IOException {
        final AdType adType = objectMapper.readValue(getClass().getResource(AD_TYPE2_JSON_PATH), AdType.class);
        saveResource(AD_TYPES_BASE_URL, adType, AdType.class);

        given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(adType)
                .when()
                    .post(AD_TYPES_BASE_URL)
                .then()
                    .statusCode(HttpStatus.SC_BAD_REQUEST);
    }
}
