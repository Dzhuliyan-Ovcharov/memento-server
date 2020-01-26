package com.memento.web.endpoint.integration;

import com.memento.model.EstateType;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.util.Set;

import static com.memento.web.RequestUrlConstant.ESTATE_TYPES_BASE_URL;
import static org.assertj.core.api.Assertions.assertThat;

public class EstateTypeApiControllerIntTest extends BaseApiControllerIntTest {

    @Test
    public void crudHappyEstateType() {
        final EstateType toSave = prepareEstateType();
        final EstateType savedEstateType = saveResource(toSave, ESTATE_TYPES_BASE_URL, EstateType.class);
        final EstateType expectedToSave = toSave.toBuilder()
                .id(savedEstateType.getId())
                .build();

        final Set<EstateType> resultAfterSave = Set.copyOf(getAll(ESTATE_TYPES_BASE_URL, EstateType.class));
        assertThat(resultAfterSave).contains(expectedToSave);

        final EstateType toUpdate = toSave.toBuilder()
                .id(savedEstateType.getId())
                .type(generateRandomString())
                .build();
        updateResource(toUpdate, ESTATE_TYPES_BASE_URL + "/" + toUpdate.getId(), HttpStatus.SC_OK);

        final Set<EstateType> resultAfterUpdate = Set.copyOf(getAll(ESTATE_TYPES_BASE_URL, EstateType.class));
        assertThat(resultAfterUpdate)
                .contains(toUpdate)
                .doesNotContain(expectedToSave);
    }

    @Test
    public void saveDuplicateEstateTypeAndExpect400() {
        final EstateType toSave = prepareEstateType();
        saveResource(toSave, ESTATE_TYPES_BASE_URL, HttpStatus.SC_OK);
        saveResource(toSave, ESTATE_TYPES_BASE_URL, HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void updateDuplicateEstateTypeAndExpect400() {
        final EstateType firstToSave = prepareEstateType();
        final EstateType firstSaved = saveResource(firstToSave, ESTATE_TYPES_BASE_URL, EstateType.class);

        final EstateType secondToSave = prepareEstateType();
        saveResource(secondToSave, ESTATE_TYPES_BASE_URL, HttpStatus.SC_OK);

        final EstateType toUpdate = firstSaved.toBuilder()
                .type(secondToSave.getType())
                .build();
        updateResource(toUpdate, ESTATE_TYPES_BASE_URL + "/" + toUpdate.getId(), HttpStatus.SC_BAD_REQUEST);
    }

    private EstateType prepareEstateType() {
        return EstateType.builder()
                .type(generateRandomString())
                .build();
    }
}
