package com.memento.web.endpoint.integration;

import com.memento.model.City;
import com.memento.model.Neighborhood;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static com.memento.web.RequestUrlConstant.CITIES_BASE_URL;
import static org.assertj.core.api.Assertions.assertThat;

public class CityApiControllerIntTest extends BaseApiControllerIntTest {

    private static final String CITY_NAME_1 = "City";
    private static final String CITY_NAME_2 = "City2";
    private static final String CITY_NAME_3 = "City3";
    private static final String CITY_NAME_4 = "City4";
    private static final String CITY_NAME_5 = "City5";
    private static final String NEIGHBORHOOD_NAME_1 = "Neighborhood";
    private static final String NEIGHBORHOOD_NAME_2 = "Neighborhood2";
    private static final String NEIGHBORHOOD_NAME_3 = "Neighborhood3";
    private static final String NEIGHBORHOOD_NAME_4 = "Neighborhood4";

    @Test
    public void crudHappyCity() {
        final City toSave = prepareCity(CITY_NAME_1, NEIGHBORHOOD_NAME_1);
        final City savedCity = saveResource(toSave, CITIES_BASE_URL, City.class);
        final City expectedToSave = toSave.toBuilder()
                .id(savedCity.getId())
                .build();

        final City resultAfterSave = getResource(CITIES_BASE_URL + "/" + expectedToSave.getId(), City.class);
        assertThat(resultAfterSave).isEqualTo(expectedToSave);

        final City toUpdate = resultAfterSave.toBuilder()
                .name(CITY_NAME_2)
                .build();
        updateResource(toUpdate, CITIES_BASE_URL + "/" + toUpdate.getId(), HttpStatus.SC_OK);

        final Set<City> resultAfterUpdate = Set.copyOf(getAll(CITIES_BASE_URL, City.class));
        assertThat(resultAfterUpdate)
                .contains(toUpdate)
                .doesNotContain(expectedToSave);
    }

    @Test
    public void saveDuplicateCityAndExpect400() {
        final City toSave = prepareCity(CITY_NAME_3, NEIGHBORHOOD_NAME_2);
        saveResource(toSave, CITIES_BASE_URL, HttpStatus.SC_OK);
        saveResource(toSave, CITIES_BASE_URL, HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void updateDuplicateCityAndExpect400() {
        final City firstToSave = prepareCity(CITY_NAME_4, NEIGHBORHOOD_NAME_3);
        final City firstSaved = saveResource(firstToSave, CITIES_BASE_URL, City.class);

        final City secondToSave = prepareCity(CITY_NAME_5, NEIGHBORHOOD_NAME_4);
        saveResource(secondToSave, CITIES_BASE_URL, HttpStatus.SC_OK);

        final City toUpdate = firstSaved.toBuilder()
                .name(secondToSave.getName())
                .build();
        updateResource(toUpdate, CITIES_BASE_URL + "/" + toUpdate.getId(), HttpStatus.SC_BAD_REQUEST);
    }

    private City prepareCity(final String cityName, final String neighborhoodName) {
        final City city = City.builder()
                .name(cityName)
                .neighborhoods(new HashSet<>())
                .build();
        final Neighborhood neighborhood = Neighborhood.builder()
                .name(neighborhoodName)
                .city(city)
                .build();
        city.getNeighborhoods().add(neighborhood);
        return city;
    }
}
