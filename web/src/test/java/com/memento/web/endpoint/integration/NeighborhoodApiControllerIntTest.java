package com.memento.web.endpoint.integration;

import com.memento.model.City;
import com.memento.model.Neighborhood;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static com.memento.web.RequestUrlConstant.CITIES_BASE_URL;
import static com.memento.web.RequestUrlConstant.NEIGHBORHOODS_BASE_URL;
import static com.memento.web.constant.JsonPathConstant.CITY_JSON_PATH;
import static org.assertj.core.api.Assertions.assertThat;

public class NeighborhoodApiControllerIntTest extends BaseApiControllerIntTest {

    @Test
    public void crudHappyNeighborhood() throws IOException {
        final City city = objectMapper.readValue(getClass().getResource(CITY_JSON_PATH), City.class);
        final City savedCity = saveResource(CITIES_BASE_URL, city, City.class);
        final Neighborhood savedNeighborhood = savedCity.getNeighborhoods().iterator().next();
        final Neighborhood expectedToSave = city.getNeighborhoods().iterator().next()
                .toBuilder()
                .id(savedNeighborhood.getId())
                .build();

        final List<Neighborhood> neighborhoods = getAll(NEIGHBORHOODS_BASE_URL + "/city/name/" + expectedToSave.getCity().getName(), Neighborhood.class);
        assertThat(neighborhoods).contains(expectedToSave);
    }
}
