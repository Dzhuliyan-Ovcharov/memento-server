package com.memento.web.endpoint;

import com.memento.model.City;
import com.memento.model.Neighborhood;
import com.memento.model.Permission;
import com.memento.service.CityService;
import com.memento.shared.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Collections;
import java.util.Set;

import static com.memento.web.RequestUrlConstant.CITIES_BASE_URL;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CityApiController.class)
public class CityApiControllerTest extends BaseApiControllerTest {

    private static final Long id = 1L;

    private City city;

    @MockBean
    private CityService cityService;

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
                get(CITIES_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].*", hasSize(3)))
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].name", is("City")))
                .andExpect(jsonPath("$.[0].neighborhoods", hasSize(1)))
                .andExpect(jsonPath("$.[0].neighborhoods[0].*", hasSize(2)))
                .andExpect(jsonPath("$.[0].neighborhoods[0].id", is(2)))
                .andExpect(jsonPath("$.[0].neighborhoods[0].name", is("Neighborhood")));

        verify(cityService, times(1)).getAll();
    }

    @Test
    public void verifyGetAllCitiesWhenCitiesAreNotPresentAndExpect200() throws Exception {
        when(cityService.getAll()).thenReturn(Collections.emptySet());

        mockMvc.perform(
                get(CITIES_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(empty())));

        verify(cityService, times(1)).getAll();
    }

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifySaveAndExpect200() throws Exception {
        final String jsonString = objectMapper.writeValueAsString(city);

        when(cityService.save(any(City.class))).thenReturn(city);

        mockMvc.perform(
                post(CITIES_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("City")))
                .andExpect(jsonPath("$.neighborhoods", hasSize(1)))
                .andExpect(jsonPath("$.neighborhoods[0].*", hasSize(2)))
                .andExpect(jsonPath("$.neighborhoods[0].id", is(2)))
                .andExpect(jsonPath("$.neighborhoods[0].name", is("Neighborhood")));

        verify(cityService, times(1)).save(any(City.class));
    }

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifySaveWhenCityIsNullAndExpect400() throws Exception {
        final String jsonString = objectMapper.writeValueAsString(null);

        mockMvc.perform(
                post(CITIES_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isBadRequest());

        verify(cityService, never()).save(any(City.class));
    }

    @Test
    @WithMockUser(authorities = {Permission.Value.AGENCY, Permission.Value.BUYER})
    public void verifySaveWhenUserIsNotAuthorizedAndExpect403() throws Exception {
        final String jsonString = objectMapper.writeValueAsString(city);

        mockMvc.perform(
                post(CITIES_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isForbidden());

        verify(cityService, never()).save(any(City.class));
    }

    @Test
    public void verifySaveWhenUserIsNotAuthenticatedAndExpect401() throws Exception {
        final String jsonString = objectMapper.writeValueAsString(city);

        mockMvc.perform(
                post(CITIES_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isUnauthorized());

        verify(cityService, never()).save(any(City.class));
    }

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifyUpdateWhenCityIsFoundAndExpect200() throws Exception {
        final String jsonString = objectMapper.writeValueAsString(city);

        when(cityService.update(anyLong(), any(City.class))).thenReturn(city);

        mockMvc.perform(
                put(CITIES_BASE_URL + "/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("City")))
                .andExpect(jsonPath("$.neighborhoods", hasSize(1)))
                .andExpect(jsonPath("$.neighborhoods[0].*", hasSize(2)))
                .andExpect(jsonPath("$.neighborhoods[0].id", is(2)))
                .andExpect(jsonPath("$.neighborhoods[0].name", is("Neighborhood")));

        verify(cityService, times(1)).update(eq(id), any(City.class));
    }

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifyUpdateWhenCityIsNotFoundAndExpect404() throws Exception {
        final String jsonString = objectMapper.writeValueAsString(city);

        when(cityService.update(anyLong(), any(City.class))).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(
                put(CITIES_BASE_URL + "/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isNotFound());

        verify(cityService, times(1)).update(eq(id), any(City.class));
    }

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifyUpdateWhenCityIsNullAndExpect400() throws Exception {
        final String jsonString = objectMapper.writeValueAsString(null);

        mockMvc.perform(
                put(CITIES_BASE_URL + "/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isBadRequest());

        verify(cityService, never()).update(eq(id), any(City.class));
    }

    @Test
    @WithMockUser(authorities = {Permission.Value.AGENCY, Permission.Value.BUYER})
    public void verifyUpdateWhenUserIsNotAuthorizedAndExpect403() throws Exception {
        final String jsonString = objectMapper.writeValueAsString(city);

        mockMvc.perform(
                put(CITIES_BASE_URL + "/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isForbidden());

        verify(cityService, never()).update(eq(id), any(City.class));
    }

    @Test
    public void verifyUpdateWhenUserIsNotAuthenticatedAndExpect401() throws Exception {
        final String jsonString = objectMapper.writeValueAsString(city);

        mockMvc.perform(
                put(CITIES_BASE_URL + "/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isUnauthorized());

        verify(cityService, never()).update(eq(id), any(City.class));
    }
}
