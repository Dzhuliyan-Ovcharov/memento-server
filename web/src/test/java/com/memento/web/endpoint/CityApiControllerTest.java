package com.memento.web.endpoint;

import com.memento.model.City;
import com.memento.model.Neighborhood;
import com.memento.model.Permission;
import com.memento.service.impl.CityServiceImpl;
import com.memento.shared.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Collections;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CityApiController.class)
public class CityApiControllerTest extends BaseApiControllerTest {

    @MockBean
    private CityServiceImpl cityService;

    private City city;

    @Before
    public void init() {
        final Neighborhood neighborhood = Neighborhood.builder()
                .id(2L)
                .name("Neighborhood")
                .build();

        city = City.builder()
                .id(1L)
                .name("City")
                .neighborhoods(Collections.singleton(neighborhood))
                .build();
    }

    @Test
    public void verifyGetAllCitiesAndExpect200() throws Exception {
        final Set<City> cities = Collections.singleton(city);

        when(cityService.getAll()).thenReturn(cities);

        mockMvc.perform(
                get("/api/city/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$.[0].length()", is(3)))
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].name", is("City")))
                .andExpect(jsonPath("$.[0].neighborhoods.length()", is(1)))
                .andExpect(jsonPath("$.[0].neighborhoods[0].length()", is(2)))
                .andExpect(jsonPath("$.[0].neighborhoods[0].id", is(2)))
                .andExpect(jsonPath("$.[0].neighborhoods[0].name", is("Neighborhood")));

        verify(cityService, times(1)).getAll();
    }

    @Test
    public void verifyGetAllCitiesWhenCitiesAreNotPresentAndExpect200() throws Exception {
        when(cityService.getAll()).thenReturn(Collections.emptySet());

        mockMvc.perform(
                get("/api/city/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(0)));

        verify(cityService, times(1)).getAll();
    }

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifySaveAndExpect200() throws Exception {
        final String jsonString = objectMapper.writeValueAsString(city);

        when(cityService.save(any(City.class))).thenReturn(city);

        mockMvc.perform(
                post("/api/city/save")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("City")))
                .andExpect(jsonPath("$.neighborhoods.length()", is(1)))
                .andExpect(jsonPath("$.neighborhoods[0].length()", is(2)))
                .andExpect(jsonPath("$.neighborhoods[0].id", is(2)))
                .andExpect(jsonPath("$.neighborhoods[0].name", is("Neighborhood")));

        verify(cityService, times(1)).save(any(City.class));
    }

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifySaveWhenCityIsNullAndExpect400() throws Exception {
        final String jsonString = objectMapper.writeValueAsString(null);

        mockMvc.perform(
                post("/api/city/save")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isBadRequest());

        verify(cityService, never()).save(any(City.class));
    }

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifyUpdateWhenCityIsFoundAndExpect200() throws Exception {
        final String jsonString = objectMapper.writeValueAsString(city);

        when(cityService.update(any(City.class))).thenReturn(city);

        mockMvc.perform(
                put("/api/city/update")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("City")))
                .andExpect(jsonPath("$.neighborhoods.length()", is(1)))
                .andExpect(jsonPath("$.neighborhoods[0].length()", is(2)))
                .andExpect(jsonPath("$.neighborhoods[0].id", is(2)))
                .andExpect(jsonPath("$.neighborhoods[0].name", is("Neighborhood")));

        verify(cityService, times(1)).update(any(City.class));
    }

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifyUpdateWhenCityIsNotFoundAndExpect404() throws Exception {
        final String jsonString = objectMapper.writeValueAsString(city);

        when(cityService.update(any(City.class))).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(
                put("/api/city/update")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isNotFound());

        verify(cityService, times(1)).update(any(City.class));
    }

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifyUpdateWhenCityIsNullAndExpect400() throws Exception {
        final String jsonString = objectMapper.writeValueAsString(null);

        mockMvc.perform(
                put("/api/city/update")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isBadRequest());

        verify(cityService, never()).update(any(City.class));
    }
}
