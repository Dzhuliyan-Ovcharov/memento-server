package com.memento.web.endpoint.integration;

import com.memento.model.Floor;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.util.Set;

import static com.memento.web.RequestUrlConstant.FLOORS_BASE_URL;
import static org.assertj.core.api.Assertions.assertThat;

public class FloorApiControllerIntTest extends BaseApiControllerIntTest {

    private static final Integer FLOOR_NUMBER_1 = 1;
    private static final Integer FLOOR_NUMBER_2 = 2;

    @Test
    public void crudHappyFloor() {
        final Floor toSave = prepareFloor(FLOOR_NUMBER_1);
        final Floor savedFloor = saveResource(toSave, FLOORS_BASE_URL, Floor.class);
        final Floor expectedToSave = toSave.toBuilder()
                .id(savedFloor.getId())
                .build();

        final Floor foundByNumber = getResource(FLOORS_BASE_URL + "/number/" + expectedToSave.getNumber(), Floor.class);
        assertThat(foundByNumber).isEqualTo(expectedToSave);

        final Set<Floor> floors = Set.copyOf(getAll(FLOORS_BASE_URL, Floor.class));
        assertThat(floors).contains(expectedToSave);
    }

    @Test
    public void saveDuplicateFloorAndExpect400() {
        final Floor toSave = prepareFloor(FLOOR_NUMBER_2);
        saveResource(toSave, FLOORS_BASE_URL, HttpStatus.SC_OK);
        saveResource(toSave, FLOORS_BASE_URL, HttpStatus.SC_BAD_REQUEST);
    }

    private Floor prepareFloor(final Integer number) {
        return Floor.builder()
                .number(number)
                .build();
    }
}
