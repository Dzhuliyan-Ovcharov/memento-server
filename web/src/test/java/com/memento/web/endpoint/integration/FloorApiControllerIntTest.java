package com.memento.web.endpoint.integration;

import com.memento.model.Floor;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Set;

import static com.memento.web.RequestUrlConstant.FLOORS_BASE_URL;
import static com.memento.web.constant.JsonPathConstant.FLOOR2_JSON_PATH;
import static com.memento.web.constant.JsonPathConstant.FLOOR_JSON_PATH;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class FloorApiControllerIntTest extends BaseApiControllerIntTest {

    @Test
    public void crudHappyFloor() throws IOException {
        final Floor toSave = objectMapper.readValue(getClass().getResource(FLOOR_JSON_PATH), Floor.class);
        final Floor savedFloor = saveResource(FLOORS_BASE_URL, toSave, Floor.class);
        final Floor expectedToSave = toSave.toBuilder()
                .id(savedFloor.getId())
                .build();

        final Floor foundByNumber = getResource(FLOORS_BASE_URL + "/number/" + expectedToSave.getNumber(), Floor.class);
        assertThat(foundByNumber).isEqualTo(expectedToSave);

        final Set<Floor> floors = Set.copyOf(getAll(FLOORS_BASE_URL, Floor.class));
        assertThat(floors).contains(expectedToSave);
    }

    @Test
    public void saveDuplicateFloorAndExpect400() throws IOException {
        final Floor floor = objectMapper.readValue(getClass().getResource(FLOOR2_JSON_PATH), Floor.class);
        saveResource(FLOORS_BASE_URL, floor, Floor.class);

        given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(floor)
                .when()
                    .post(FLOORS_BASE_URL)
                .then()
                    .statusCode(HttpStatus.SC_BAD_REQUEST);
    }
}
