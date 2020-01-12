package com.memento.web.endpoint;

import com.memento.model.City;
import com.memento.model.Neighborhood;
import com.memento.service.NeighborhoodService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.Collections;

import static com.memento.web.RequestUrlConstant.NEIGHBORHOODS_BASE_URL;
import static com.memento.web.constant.JsonPathConstant.NEIGHBORHOOD_COLLECTION_JSON_PATH;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = NeighborhoodApiController.class)
public class NeighborhoodApiControllerTest extends BaseApiControllerTest {

    private static final Long ID = 1L;
    private static final String NAME = "Neighborhood name";
    private static final String CITY_NAME = "City name";

    private Neighborhood neighborhood;

    @MockBean
    private NeighborhoodService neighborhoodService;

    @Before
    public void init() {
        neighborhood = Neighborhood.builder()
                .id(ID)
                .name(NAME)
                .city(City.builder().build())
                .build();
    }

    @Test
    public void verifyFindAllByCityNameAndExpect200() throws Exception {
        final String jsonResponse = loadJsonResource(NEIGHBORHOOD_COLLECTION_JSON_PATH, Neighborhood[].class);

        when(neighborhoodService.findAllByCityName(CITY_NAME)).thenReturn(Collections.singletonList(neighborhood));

        mockMvc.perform(
                get(NEIGHBORHOODS_BASE_URL + "/city/name/" + CITY_NAME)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonResponse, true));

        verify(neighborhoodService, times(1)).findAllByCityName(CITY_NAME);
    }

    @Test
    public void verifyFindAllByCityNameWhenCityIsNotFoundAndExpect200() throws Exception {
        when(neighborhoodService.findAllByCityName(CITY_NAME)).thenReturn(Collections.emptyList());

        mockMvc.perform(
                get(NEIGHBORHOODS_BASE_URL + "/city/name/" + CITY_NAME)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(EMPTY_JSON_COLLECTION, true));

        verify(neighborhoodService, times(1)).findAllByCityName(CITY_NAME);
    }
}
