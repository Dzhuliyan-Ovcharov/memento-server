package com.memento.web.endpoint;

import com.memento.model.City;
import com.memento.model.Neighborhood;
import com.memento.service.NeighborhoodService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = NeighborhoodApiController.class)
public class NeighborhoodApiControllerTest extends BaseApiControllerTest {

    @MockBean
    private NeighborhoodService neighborhoodService;

    @Test
    public void verifyFindAllByCityNameWhenCityIsFoundAndExpect200() throws Exception {
        final List<Neighborhood> neighborhoods = Collections.singletonList(Neighborhood.builder()
                .id(1L)
                .name("Neighborhood")
                .city(City.builder().build())
                .build());

        when(neighborhoodService.findAllByCityName(anyString())).thenReturn(neighborhoods);

        mockMvc.perform(
                get("/api/neighborhood/city/name/city_name")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].*", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].name", is("Neighborhood")));

        verify(neighborhoodService, times(1)).findAllByCityName(anyString());
    }

    @Test
    public void verifyFindAllByCityNameWhenCityIsNotFoundAndExpect200() throws Exception {
        when(neighborhoodService.findAllByCityName(anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(
                get("/api/neighborhood/city/name/city_name")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(empty())));

        verify(neighborhoodService, times(1)).findAllByCityName(anyString());
    }

}
