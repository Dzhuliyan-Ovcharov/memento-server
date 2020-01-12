package com.memento.web.endpoint;

import com.memento.model.EstateType;
import com.memento.model.Permission;
import com.memento.service.EstateTypeService;
import com.memento.shared.exception.BadRequestException;
import com.memento.shared.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import static com.memento.web.RequestUrlConstant.ESTATE_TYPES_BASE_URL;
import static com.memento.web.constant.JsonPathConstant.ESTATE_TYPE_COLLECTION_JSON_PATH;
import static com.memento.web.constant.JsonPathConstant.ESTATE_TYPE_JSON_PATH;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EstateTypeApiController.class)
public class EstateTypeApiControllerTest extends BaseApiControllerTest {

    private static final Long ID = 1L;

    private EstateType estateType;

    @MockBean
    private EstateTypeService estateTypeService;

    @Before
    public void init() throws IOException {
        estateType = objectMapper.readValue(getClass().getResource(ESTATE_TYPE_JSON_PATH), EstateType.class);
    }

    @Test
    public void verifyGetAllEstateTypesAndExpect200() throws Exception {
        final Set<EstateType> estateTypes = objectMapper.readValue(
                getClass().getResource(ESTATE_TYPE_COLLECTION_JSON_PATH),
                objectMapper.getTypeFactory().constructCollectionType(Set.class, EstateType.class));

        final String jsonResponse = objectMapper.writeValueAsString(estateTypes);

        when(estateTypeService.getAll()).thenReturn(estateTypes);

        mockMvc.perform(
                get(ESTATE_TYPES_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonResponse, true));

        verify(estateTypeService, times(1)).getAll();
    }

    @Test
    public void verifyGetAllWhenEstateTypesAreNotPresentAndExpect200() throws Exception {
        when(estateTypeService.getAll()).thenReturn(Collections.emptySet());

        mockMvc.perform(
                get(ESTATE_TYPES_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(EMPTY_JSON_COLLECTION, true));

        verify(estateTypeService, times(1)).getAll();
    }

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifySaveAndExpect200() throws Exception {
        final String jsonRequest = objectMapper.writeValueAsString(estateType);

        when(estateTypeService.save(any(EstateType.class))).thenReturn(estateType);

        mockMvc.perform(
                post(ESTATE_TYPES_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonRequest, true));

        verify(estateTypeService, times(1)).save(estateType);
    }

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifySaveWhenEstateTypeIsNullAndExpect400() throws Exception {
        mockMvc.perform(
                post(ESTATE_TYPES_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(EMPTY_JSON))
                .andExpect(status().isBadRequest());

        verify(estateTypeService, never()).save(any(EstateType.class));
    }

    @Test
    @WithMockUser(authorities = {Permission.Value.AGENCY, Permission.Value.BUYER})
    public void verifySaveWhenUserIsNotAuthorizedAndExpect403() throws Exception {
        final String jsonRequest = objectMapper.writeValueAsString(estateType);

        mockMvc.perform(
                post(ESTATE_TYPES_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isForbidden());

        verify(estateTypeService, never()).save(any(EstateType.class));
    }

    @Test
    public void verifySaveWhenUserIsNotAuthenticatedAndExpect401() throws Exception {
        final String jsonRequest = objectMapper.writeValueAsString(estateType);

        mockMvc.perform(
                post(ESTATE_TYPES_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUnauthorized());

        verify(estateTypeService, never()).save(any(EstateType.class));
    }

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifyUpdateAndExpect200() throws Exception {
        final String jsonRequest = objectMapper.writeValueAsString(estateType);

        when(estateTypeService.update(eq(ID), any(EstateType.class))).thenReturn(estateType);

        mockMvc.perform(
                put(ESTATE_TYPES_BASE_URL + "/" + ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonRequest, true));

        verify(estateTypeService, times(1)).update(ID, estateType);
    }

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifyUpdateWhenEstateTypeIdDoesNotMatchWithPassedIdAndExpect400() throws Exception {
        final String jsonRequest = objectMapper.writeValueAsString(estateType);

        when(estateTypeService.update(eq(ID), any(EstateType.class))).thenThrow(BadRequestException.class);

        mockMvc.perform(
                put(ESTATE_TYPES_BASE_URL + "/" + ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());

        verify(estateTypeService, times(1)).update(eq(ID), any(EstateType.class));
    }

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifyUpdateWhenEstateTypeIsNotFoundAndExpect404() throws Exception {
        final String jsonRequest = objectMapper.writeValueAsString(estateType);

        when(estateTypeService.update(eq(ID), any(EstateType.class))).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(
                put(ESTATE_TYPES_BASE_URL + "/" + ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound());

        verify(estateTypeService, times(1)).update(eq(ID), any(EstateType.class));
    }

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifyUpdateWhenEstateTypeIsNullAndExpect400() throws Exception {
        mockMvc.perform(
                put(ESTATE_TYPES_BASE_URL + "/" + ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(EMPTY_JSON))
                .andExpect(status().isBadRequest());

        verify(estateTypeService, never()).update(eq(ID), any(EstateType.class));
    }

    @Test
    @WithMockUser(authorities = {Permission.Value.AGENCY, Permission.Value.BUYER})
    public void verifyUpdateWhenUserIsNotAuthorizedAndExpect403() throws Exception {
        final String jsonRequest = objectMapper.writeValueAsString(estateType);

        mockMvc.perform(
                put(ESTATE_TYPES_BASE_URL + "/" + ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isForbidden());

        verify(estateTypeService, never()).update(eq(ID), any(EstateType.class));
    }

    @Test
    public void verifyUpdateWhenUserIsNotAuthenticatedAndExpect401() throws Exception {
        final String jsonRequest = objectMapper.writeValueAsString(estateType);

        mockMvc.perform(
                put(ESTATE_TYPES_BASE_URL + "/" + ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUnauthorized());

        verify(estateTypeService, never()).update(eq(ID), any(EstateType.class));
    }
}
