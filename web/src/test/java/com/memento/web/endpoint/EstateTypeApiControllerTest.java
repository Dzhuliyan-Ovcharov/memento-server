package com.memento.web.endpoint;

import com.memento.model.EstateType;
import com.memento.model.Permission;
import com.memento.service.EstateTypeService;
import com.memento.shared.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Collections;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EstateTypeApiController.class)
public class EstateTypeApiControllerTest extends BaseApiControllerTest {

    @MockBean
    private EstateTypeService estateTypeService;

    private EstateType estateType;

    @Before
    public void init() {
        estateType = EstateType.builder()
                .id(1L)
                .type("Estate type")
                .estates(Collections.emptySet())
                .build();
    }

    @Test
    public void verifyGetAllEstateTypesAndExpect200() throws Exception {
        final Set<EstateType> estateTypes = Collections.singleton(estateType);

        when(estateTypeService.getAll()).thenReturn(estateTypes);

        mockMvc.perform(
                get("/api/estate/type/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].*", hasSize(3)))
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].type", is("Estate type")))
                .andExpect(jsonPath("$.[0].estates", is(empty())));

        verify(estateTypeService, times(1)).getAll();
    }

    @Test
    public void verifyGetAllWhenEstateTypesAreNotPresentAndExpect200() throws Exception {
        when(estateTypeService.getAll()).thenReturn(Collections.emptySet());

        mockMvc.perform(
                get("/api/estate/type/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(empty())));

        verify(estateTypeService, times(1)).getAll();
    }

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifySaveAndExpect200() throws Exception {
        final String jsonString = objectMapper.writeValueAsString(estateType);

        when(estateTypeService.save(any(EstateType.class))).thenReturn(estateType);

        mockMvc.perform(
                post("/api/estate/type/save")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.type", is("Estate type")))
                .andExpect(jsonPath("$.estates", is(empty())));

        verify(estateTypeService, times(1)).save(any(EstateType.class));
    }

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifySaveWhenEstateTypeIsNullAndExpect400() throws Exception {
        final String jsonString = objectMapper.writeValueAsString(null);

        mockMvc.perform(
                post("/api/estate/type/save")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isBadRequest());

        verify(estateTypeService, never()).save(any(EstateType.class));
    }

    @Test
    @WithMockUser(authorities = {Permission.Value.AGENCY, Permission.Value.BUYER})
    public void verifySaveWhenUserIsNotAuthorizedAndExpect403() throws Exception {
        final String jsonString = objectMapper.writeValueAsString(estateType);

        mockMvc.perform(
                post("/api/estate/type/save")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isForbidden());

        verify(estateTypeService, never()).save(any(EstateType.class));
    }

    @Test
    public void verifySaveWhenUserIsNotAuthenticatedAndExpect401() throws Exception {
        final String jsonString = objectMapper.writeValueAsString(estateType);

        mockMvc.perform(
                post("/api/estate/type/save")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isUnauthorized());

        verify(estateTypeService, never()).save(any(EstateType.class));
    }

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifyUpdateWhenEstateTypeIsFoundAndExpect200() throws Exception {
        final String jsonString = objectMapper.writeValueAsString(estateType);

        when(estateTypeService.update(any(EstateType.class))).thenReturn(estateType);

        mockMvc.perform(
                put("/api/estate/type/update")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.type", is("Estate type")))
                .andExpect(jsonPath("$.estates", is(empty())));

        verify(estateTypeService, times(1)).update(any(EstateType.class));
    }

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifyUpdateWhenEstateTypeIsNotFoundAndExpect404() throws Exception {
        final String jsonString = objectMapper.writeValueAsString(estateType);

        when(estateTypeService.update(any(EstateType.class))).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(
                put("/api/estate/type/update")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isNotFound());

        verify(estateTypeService, times(1)).update(any(EstateType.class));
    }

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifyUpdateWhenEstateTypeIsNullAndExpect400() throws Exception {
        final String jsonString = objectMapper.writeValueAsString(null);

        mockMvc.perform(
                put("/api/estate/type/update")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isBadRequest());

        verify(estateTypeService, never()).update(any(EstateType.class));
    }

    @Test
    @WithMockUser(authorities = {Permission.Value.AGENCY, Permission.Value.BUYER})
    public void verifyUpdateWhenUserIsNotAuthorizedAndExpect403() throws Exception {
        final String jsonString = objectMapper.writeValueAsString(estateType);

        mockMvc.perform(
                put("/api/estate/type/update")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isForbidden());

        verify(estateTypeService, never()).update(any(EstateType.class));
    }

    @Test
    public void verifyUpdateWhenUserIsNotAuthenticatedAndExpect401() throws Exception {
        final String jsonString = objectMapper.writeValueAsString(estateType);

        mockMvc.perform(
                put("/api/estate/type/update")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isUnauthorized());

        verify(estateTypeService, never()).update(any(EstateType.class));
    }
}
