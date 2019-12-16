package com.memento.web.endpoint;

import com.memento.model.AdType;
import com.memento.model.Permission;
import com.memento.service.AdTypeService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Collections;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AdTypeApiController.class)
public class AdTypeApiControllerTest extends BaseApiControllerTest {

    @MockBean
    private AdTypeService adTypeService;

    private AdType adType;

    @Before
    public void init() {
        adType = AdType.builder()
                .id(1L)
                .type("Type")
                .estates(Collections.emptySet())
                .build();
    }

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifySaveAdTypeAndExpect200() throws Exception {
        final String jsonString = objectMapper.writeValueAsString(adType);

        when(adTypeService.save(any(AdType.class))).thenReturn(adType);

        mockMvc.perform(
                post("/api/ad/type/save")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.type", is("Type")))
                .andExpect(jsonPath("$.estates", is(empty())));

        verify(adTypeService, times(1)).save(any(AdType.class));
    }

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifySaveWhenAdTypeIsNullAndExpect400() throws Exception {
        final String jsonString = objectMapper.writeValueAsString(null);

        mockMvc.perform(
                post("/api/ad/type/save")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isBadRequest());

        verify(adTypeService, never()).save(any(AdType.class));
    }

    @Test
    @WithMockUser(authorities = {Permission.Value.AGENCY, Permission.Value.BUYER})
    public void verifySaveWhenUserIsNotAuthorizedAndExpect403() throws Exception {
        final String jsonString = objectMapper.writeValueAsString(adType);

        mockMvc.perform(
                post("/api/ad/type/save")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isForbidden());

        verify(adTypeService, never()).save(any(AdType.class));
    }

    @Test
    public void verifySaveWhenUserIsNotAuthenticatedAndExpect401() throws Exception {
        final String jsonString = objectMapper.writeValueAsString(adType);

        mockMvc.perform(
                post("/api/ad/type/save")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isUnauthorized());

        verify(adTypeService, never()).save(any(AdType.class));
    }

    @Test
    public void verifyGetAllAdTypesAndExpect200() throws Exception {
        final Set<AdType> adTypes = Collections.singleton(adType);

        when(adTypeService.getAll()).thenReturn(adTypes);

        mockMvc.perform(
                get("/api/ad/type/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].*", hasSize(3)))
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].type", is("Type")))
                .andExpect(jsonPath("$.[0].estates", is(empty())));

        verify(adTypeService, times(1)).getAll();
    }

    @Test
    public void verifyGetAllAdTypesWhenAdTypesAreNotPresentAndExpect200() throws Exception {
        when(adTypeService.getAll()).thenReturn(Collections.emptySet());

        mockMvc.perform(
                get("/api/ad/type/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(empty())));

        verify(adTypeService, times(1)).getAll();
    }
}
