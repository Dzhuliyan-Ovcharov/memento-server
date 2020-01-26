package com.memento.web.endpoint.integration;

import com.memento.model.AdType;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.util.Set;

import static com.memento.web.RequestUrlConstant.AD_TYPES_BASE_URL;
import static org.assertj.core.api.Assertions.assertThat;

public class AdTypeApiControllerIntTest extends BaseApiControllerIntTest {

    @Test
    public void crudHappyAdType() {
        final AdType toSave = prepareAdType();
        final AdType savedAdType = saveResource(toSave, AD_TYPES_BASE_URL, AdType.class);
        final AdType expectedToSave = toSave.toBuilder()
                .id(savedAdType.getId())
                .build();

        final Set<AdType> resultAfterSave = Set.copyOf(getAll(AD_TYPES_BASE_URL, AdType.class));
        assertThat(resultAfterSave).contains(expectedToSave);
    }

    @Test
    public void saveDuplicateAdTypeAndExpect400() {
        final AdType toSave = prepareAdType();
        saveResource(toSave, AD_TYPES_BASE_URL, HttpStatus.SC_OK);
        saveResource(toSave, AD_TYPES_BASE_URL, HttpStatus.SC_BAD_REQUEST);
    }

    private AdType prepareAdType() {
        return AdType.builder()
                .type(generateRandomString())
                .build();
    }
}
