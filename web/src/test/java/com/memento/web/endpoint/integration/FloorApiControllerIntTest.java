package com.memento.web.endpoint.integration;

import com.memento.model.Floor;
import org.apache.commons.lang3.RandomUtils;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.util.Set;

import static com.memento.web.RequestUrlConstant.FLOORS_BASE_URL;
import static org.assertj.core.api.Assertions.assertThat;

public class FloorApiControllerIntTest extends BaseApiControllerIntTest {

    @Test
    public void crudHappyFloor() {
        final Floor toSave = prepareFloor();
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
        final Floor toSave = prepareFloor();
        saveResource(toSave, FLOORS_BASE_URL, HttpStatus.SC_OK);
        saveResource(toSave, FLOORS_BASE_URL, HttpStatus.SC_BAD_REQUEST);
    }

    private Floor prepareFloor() {
        return Floor.builder()
                .number(RandomUtils.nextInt())
                .build();
    }
}
