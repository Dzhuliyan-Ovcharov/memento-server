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

import static com.memento.web.RequestUrlConstant.AD_TYPES_BASE_URL;
import static com.memento.web.constant.JsonPathConstant.AD_TYPE_COLLECTION_JSON_PATH;
import static com.memento.web.constant.JsonPathConstant.AD_TYPE_JSON_PATH;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdTypeApiController.class)
public class AdTypeApiControllerTest extends BaseApiControllerTest {

    private static final Long ID = 1L;
    private static final String TYPE = "Ad type";

    private AdType adType;

    @MockBean
    private AdTypeService adTypeService;

    @Before
    public void init() {
        adType = AdType.builder()
                .id(ID)
                .type(TYPE)
                .estates(Collections.emptySet())
                .build();
    }

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifySaveAdTypeAndExpect200() throws Exception {
        final String jsonRequest = loadJsonResource(AD_TYPE_JSON_PATH, AdType.class);

        when(adTypeService.save(any(AdType.class))).thenReturn(adType);

        mockMvc.perform(
                post(AD_TYPES_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonRequest, true));

        verify(adTypeService, times(1)).save(adType);
    }

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifySaveWhenAdTypeIsNullAndExpect400() throws Exception {
        mockMvc.perform(
                post(AD_TYPES_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(EMPTY_JSON))
                .andExpect(status().isBadRequest());

        verify(adTypeService, never()).save(any(AdType.class));
    }

    @Test
    @WithMockUser(authorities = {Permission.Value.AGENCY, Permission.Value.BUYER})
    public void verifySaveWhenUserIsNotAuthorizedAndExpect403() throws Exception {
        final String jsonRequest = loadJsonResource(AD_TYPE_JSON_PATH, AdType.class);

        mockMvc.perform(
                post(AD_TYPES_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isForbidden());

        verify(adTypeService, never()).save(any(AdType.class));
    }

    @Test
    public void verifySaveWhenUserIsNotAuthenticatedAndExpect401() throws Exception {
        final String jsonRequest = loadJsonResource(AD_TYPE_JSON_PATH, AdType.class);

        mockMvc.perform(
                post(AD_TYPES_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUnauthorized());

        verify(adTypeService, never()).save(any(AdType.class));
    }

    @Test
    public void verifyGetAllAdTypesAndExpect200() throws Exception {
        final String jsonResponse = loadJsonResource(AD_TYPE_COLLECTION_JSON_PATH, AdType[].class);

        when(adTypeService.getAll()).thenReturn(Collections.singleton(adType));

        mockMvc.perform(
                get(AD_TYPES_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonResponse, true));

        verify(adTypeService, times(1)).getAll();
    }

    @Test
    public void verifyGetAllAdTypesWhenAdTypesAreNotPresentAndExpect200() throws Exception {
        when(adTypeService.getAll()).thenReturn(Collections.emptySet());

        mockMvc.perform(
                get(AD_TYPES_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(EMPTY_JSON_COLLECTION, true));

        verify(adTypeService, times(1)).getAll();
    }
}
