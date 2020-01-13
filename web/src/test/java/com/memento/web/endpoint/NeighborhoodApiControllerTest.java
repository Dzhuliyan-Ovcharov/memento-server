package com.memento.web.endpoint;

import com.memento.model.Neighborhood;
import com.memento.service.NeighborhoodService;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.List;

import static com.memento.web.RequestUrlConstant.NEIGHBORHOODS_BASE_URL;
import static com.memento.web.constant.JsonPathConstant.NEIGHBORHOOD_COLLECTION_JSON_PATH;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = NeighborhoodApiController.class)
public class NeighborhoodApiControllerTest extends BaseApiControllerTest {

    private static final String CITY_NAME = "City name";

    @MockBean
    private NeighborhoodService neighborhoodService;

    @Test
    public void verifyFindAllByCityNameAndExpect200() throws Exception {
        final List<Neighborhood> neighborhoods = objectMapper.readValue(
                getClass().getResource(NEIGHBORHOOD_COLLECTION_JSON_PATH),
                objectMapper.getTypeFactory().constructCollectionType(List.class, Neighborhood.class));

        final String jsonResponse = objectMapper.writeValueAsString(neighborhoods);

        when(neighborhoodService.findAllByCityName(CITY_NAME)).thenReturn(neighborhoods);

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
