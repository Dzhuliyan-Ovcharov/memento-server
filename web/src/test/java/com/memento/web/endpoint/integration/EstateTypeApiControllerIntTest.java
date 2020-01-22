package com.memento.web.endpoint.integration;

import com.memento.model.EstateType;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Set;

import static com.memento.web.RequestUrlConstant.ESTATE_TYPES_BASE_URL;
import static com.memento.web.constant.JsonPathConstant.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class EstateTypeApiControllerIntTest extends BaseApiControllerIntTest {

    @Test
    public void crudHappyEstateType() throws IOException {
        final EstateType toSave = objectMapper.readValue(getClass().getResource(ESTATE_TYPE_JSON_PATH), EstateType.class);
        final EstateType savedEstateType = saveResource(ESTATE_TYPES_BASE_URL, toSave, EstateType.class);
        final EstateType expectedToSave = toSave.toBuilder()
                .id(savedEstateType.getId())
                .build();

        final Set<EstateType> resultAfterSave = Set.copyOf(getAll(ESTATE_TYPES_BASE_URL, EstateType.class));
        assertThat(resultAfterSave).contains(expectedToSave);

        final EstateType toUpdate = objectMapper.readValue(getClass().getResource(ESTATE_TYPE2_JSON_PATH), EstateType.class)
                .toBuilder()
                .id(savedEstateType.getId())
                .build();
        updateResource(ESTATE_TYPES_BASE_URL + "/" + toUpdate.getId(), toUpdate, EstateType.class);

        final Set<EstateType> resultAfterUpdate = Set.copyOf(getAll(ESTATE_TYPES_BASE_URL, EstateType.class));
        assertThat(resultAfterUpdate)
                .contains(toUpdate)
                .doesNotContain(expectedToSave);
    }

    @Test
    public void saveDuplicateEstateTypeAndExpect400() throws IOException {
        final EstateType estateType = objectMapper.readValue(getClass().getResource(ESTATE_TYPE3_JSON_PATH), EstateType.class);
        saveResource(ESTATE_TYPES_BASE_URL, estateType, EstateType.class);

        given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(estateType)
                .when()
                    .post(ESTATE_TYPES_BASE_URL)
                .then()
                    .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void updateDuplicateEstateTypeAndExpect400() throws IOException {
        final EstateType firstToSave = objectMapper.readValue(getClass().getResource(ESTATE_TYPE4_JSON_PATH), EstateType.class);
        final EstateType firstSaved = saveResource(ESTATE_TYPES_BASE_URL, firstToSave, EstateType.class);

        final EstateType secondToSave = objectMapper.readValue(getClass().getResource(ESTATE_TYPE5_JSON_PATH), EstateType.class);
        saveResource(ESTATE_TYPES_BASE_URL, secondToSave, EstateType.class);

        final EstateType toUpdate = firstSaved.toBuilder()
                .type(secondToSave.getType())
                .build();

        given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(toUpdate)
                .when()
                    .put(ESTATE_TYPES_BASE_URL + "/" + toUpdate.getId())
                .then()
                    .statusCode(HttpStatus.SC_BAD_REQUEST);
    }
}
