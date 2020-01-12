package com.memento.web.endpoint;

import com.memento.model.City;
import com.memento.model.Neighborhood;
import com.memento.model.Permission;
import com.memento.service.CityService;
import com.memento.shared.exception.BadRequestException;
import com.memento.shared.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Collections;

import static com.memento.web.RequestUrlConstant.CITIES_BASE_URL;
import static com.memento.web.constant.JsonPathConstant.CITY_COLLECTION_JSON_PATH;
import static com.memento.web.constant.JsonPathConstant.CITY_JSON_PATH;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CityApiController.class)
public class CityApiControllerTest extends BaseApiControllerTest {

    private static final Long ID = 1L;
    private static final String NEIGHBORHOOD_NAME = "Neighborhood name";
    private static final String CITY_NAME = "City name";

    private City city;

    @MockBean
    private CityService cityService;

    @Before
    public void init() {
        final Neighborhood neighborhood = Neighborhood.builder()
                .id(ID)
                .name(NEIGHBORHOOD_NAME)
                .build();

        city = City.builder()
                .id(ID)
                .name(CITY_NAME)
                .neighborhoods(Collections.singleton(neighborhood))
                .build();
    }

    @Test
    public void verifyGetAllAndExpect200() throws Exception {
        final String jsonResponse = loadJsonResource(CITY_COLLECTION_JSON_PATH, City[].class);

        when(cityService.getAll()).thenReturn(Collections.singleton(city));

        mockMvc.perform(
                get(CITIES_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonResponse, true));

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
                .andExpect(content().json(EMPTY_JSON_COLLECTION, true));

        verify(cityService, times(1)).getAll();
    }

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifySaveAndExpect200() throws Exception {
        final String jsonRequest = loadJsonResource(CITY_JSON_PATH, City.class);

        when(cityService.save(any(City.class))).thenReturn(city);

        mockMvc.perform(
                post(CITIES_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonRequest, true));

        verify(cityService, times(1)).save(city);
    }

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifySaveWhenCityIsNullAndExpect400() throws Exception {
        mockMvc.perform(
                post(CITIES_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(EMPTY_JSON))
                .andExpect(status().isBadRequest());

        verify(cityService, never()).save(any(City.class));
    }

    @Test
    @WithMockUser(authorities = {Permission.Value.AGENCY, Permission.Value.BUYER})
    public void verifySaveWhenUserIsNotAuthorizedAndExpect403() throws Exception {
        final String jsonRequest = loadJsonResource(CITY_JSON_PATH, City.class);

        mockMvc.perform(
                post(CITIES_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isForbidden());

        verify(cityService, never()).save(any(City.class));
    }

    @Test
    public void verifySaveWhenUserIsNotAuthenticatedAndExpect401() throws Exception {
        final String jsonRequest = loadJsonResource(CITY_JSON_PATH, City.class);

        mockMvc.perform(
                post(CITIES_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUnauthorized());

        verify(cityService, never()).save(any(City.class));
    }

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifyUpdateAndExpect200() throws Exception {
        final String jsonRequest = loadJsonResource(CITY_JSON_PATH, City.class);

        when(cityService.update(eq(ID), any(City.class))).thenReturn(city);

        mockMvc.perform(
                put(CITIES_BASE_URL + "/" + ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonRequest, true));

        verify(cityService, times(1)).update(ID, city);
    }

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifyUpdateWhenCityIdDoesNotMatchWithPassedIdAndExpect400() throws Exception {
        final String jsonRequest = loadJsonResource(CITY_JSON_PATH, City.class);

        when(cityService.update(eq(ID), any(City.class))).thenThrow(BadRequestException.class);

        mockMvc.perform(
                put(CITIES_BASE_URL + "/" + ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());

        verify(cityService, times(1)).update(eq(ID), any(City.class));
    }

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifyUpdateWhenCityIsNotFoundAndExpect404() throws Exception {
        final String jsonRequest = loadJsonResource(CITY_JSON_PATH, City.class);

        when(cityService.update(eq(ID), any(City.class))).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(
                put(CITIES_BASE_URL + "/" + ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound());

        verify(cityService, times(1)).update(eq(ID), any(City.class));
    }

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifyUpdateWhenCityIsNullAndExpect400() throws Exception {
        mockMvc.perform(
                put(CITIES_BASE_URL + "/" + ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(EMPTY_JSON))
                .andExpect(status().isBadRequest());

        verify(cityService, never()).update(eq(ID), any(City.class));
    }

    @Test
    @WithMockUser(authorities = {Permission.Value.AGENCY, Permission.Value.BUYER})
    public void verifyUpdateWhenUserIsNotAuthorizedAndExpect403() throws Exception {
        final String jsonRequest = loadJsonResource(CITY_JSON_PATH, City.class);

        mockMvc.perform(
                put(CITIES_BASE_URL + "/" + ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isForbidden());

        verify(cityService, never()).update(eq(ID), any(City.class));
    }

    @Test
    public void verifyUpdateWhenUserIsNotAuthenticatedAndExpect401() throws Exception {
        final String jsonRequest = loadJsonResource(CITY_JSON_PATH, City.class);

        mockMvc.perform(
                put(CITIES_BASE_URL + "/" + ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUnauthorized());

        verify(cityService, never()).update(eq(ID), any(City.class));
    }
}
