package com.memento.web.endpoint;

import com.memento.model.*;
import com.memento.service.EstateService;
import com.memento.shared.exception.ResourceNotFoundException;
import com.memento.web.dto.EstateRequest;
import com.memento.web.dto.EstateResponse;
import org.apache.commons.lang3.StringUtils;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.util.Collections;

import static com.memento.web.RequestUrlConstant.ESTATES_BASE_URL;
import static com.memento.web.constant.JsonPathConstant.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EstateApiController.class)
public class EstateApiControllerTest extends BaseApiControllerTest {

    private static final Long ID = 1L;
    private static final Double PRICE = 1000.00;
    private static final Double BUILT_UP_AREA = 100.00;
    private static final Double PURE_AREA = 50.00;
    private static final Integer FLOOR_NUMBER = 1;
    private static final String DESCRIPTION = "Description";
    private static final String ESTATE_TYPE = "Estate type";
    private static final String AD_TYPE = "Ad type";
    private static final String EMAIL = "email@email.email";
    private static final String FIRST_NAME = "First name";
    private static final String LAST_NAME = "Last name";
    private static final String IMAGE_NAME = "image.jpg";

    private Estate estate;

    private EstateRequest estateRequest;

    @SpyBean
    private ModelMapper modelMapper;

    @MockBean
    private EstateService estateService;

    @Before
    public void init() {
        final Floor floor = Floor.builder()
                .id(ID)
                .number(FLOOR_NUMBER)
                .build();

        final EstateType estateType = EstateType.builder()
                .id(ID)
                .type(ESTATE_TYPE)
                .estates(Collections.emptySet())
                .build();

        final AdType adType = AdType.builder()
                .id(ID)
                .type(AD_TYPE)
                .estates(Collections.emptySet())
                .build();

        final User user = User.builder()
                .id(ID)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .password(StringUtils.EMPTY)
                .role(Role.builder().id(ID).permission(Permission.BUYER).build())
                .estates(Collections.emptyList())
                .build();

        final Image image = Image.builder()
                .id(ID)
                .name(IMAGE_NAME)
                .build();

        estate = Estate.builder()
                .id(ID)
                .price(Money.of(CurrencyUnit.USD, BigDecimal.valueOf(PRICE)))
                .quadrature(Quadrature.of(BigDecimal.valueOf(BUILT_UP_AREA), BigDecimal.valueOf(PURE_AREA)))
                .description(DESCRIPTION)
                .floor(floor)
                .estateType(estateType)
                .adType(adType)
                .user(user)
                .images(Collections.singletonList(image))
                .build();

        estateRequest = EstateRequest.builder()
                .price(Money.of(CurrencyUnit.USD, BigDecimal.valueOf(PRICE)))
                .quadrature(Quadrature.of(BigDecimal.valueOf(BUILT_UP_AREA), BigDecimal.valueOf(PURE_AREA)))
                .description(DESCRIPTION)
                .floorNumber(FLOOR_NUMBER)
                .estateType(ESTATE_TYPE)
                .adType(AD_TYPE)
                .email(EMAIL)
                .build();
    }

    @Test
    public void verifyFindByIdAndExpect200() throws Exception {
        final String jsonResponse = loadJsonResource(ESTATE_RESPONSE_JSON_PATH, EstateResponse.class);

        when(estateService.findById(ID)).thenReturn(estate);

        mockMvc.perform(
                get(ESTATES_BASE_URL + "/" + ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonResponse, true));

        verify(estateService, times(1)).findById(ID);
        verify(modelMapper, times(1)).map(estate, EstateResponse.class);
    }

    @Test
    public void verifyFindByIdWhenIdIsNotFoundAndExpect404() throws Exception {
        when(estateService.findById(ID)).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(
                get(ESTATES_BASE_URL + "/" + ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(estateService, times(1)).findById(ID);
        verify(modelMapper, never()).map(any(Estate.class), eq(EstateResponse.class));
    }

    @Test
    public void verifyGetAllAndExpect200() throws Exception {
        final String jsonResponse = loadJsonResource(ESTATE_RESPONSE_COLLECTION_JSON_PATH, EstateResponse[].class);

        when(estateService.getAll()).thenReturn(Collections.singleton(estate));

        mockMvc.perform(
                get(ESTATES_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonResponse, true));

        verify(estateService, times(1)).getAll();
        verify(modelMapper, times(1)).map(any(Estate.class), eq(EstateResponse.class));
    }

    @Test
    public void verifyGetAllWhenEstatesAreNotPresentAndExpect200() throws Exception {
        when(estateService.getAll()).thenReturn(Collections.emptySet());

        mockMvc.perform(
                get(ESTATES_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(EMPTY_JSON_COLLECTION, true));

        verify(estateService, times(1)).getAll();
        verify(modelMapper, never()).map(any(Estate.class), eq(EstateResponse.class));
    }

    @Test
    @WithMockUser(authorities = {Permission.Value.ADMIN, Permission.Value.AGENCY})
    public void verifySaveAndExpect200() throws Exception {
        final String jsonRequest = loadJsonResource(ESTATE_REQUEST_JSON_PATH, EstateRequest.class);
        final String jsonResponse = loadJsonResource(ESTATE_RESPONSE_JSON_PATH, EstateResponse.class);

        when(estateService.save(any(Estate.class))).thenReturn(estate);

        mockMvc.perform(
                post(ESTATES_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonResponse, true));

        verify(modelMapper, times(1)).map(estateRequest, Estate.class);
        verify(estateService, times(1)).save(any(Estate.class));
        verify(modelMapper, times(1)).map(estate, EstateResponse.class);
    }

    @Test
    @WithMockUser(authorities = {Permission.Value.ADMIN, Permission.Value.AGENCY})
    public void verifySaveWhenEstateRequestIsNotValidAndExpect404() throws Exception {
        final String jsonRequest = loadJsonResource(ESTATE_REQUEST_JSON_PATH, EstateRequest.class);

        when(estateService.save(any(Estate.class))).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(
                post(ESTATES_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound());

        verify(modelMapper, times(1)).map(any(EstateRequest.class), eq(Estate.class));
        verify(estateService, times(1)).save(any(Estate.class));
        verify(modelMapper, never()).map(any(Estate.class), eq(EstateResponse.class));
    }

    @Test
    @WithMockUser(authorities = {Permission.Value.ADMIN, Permission.Value.AGENCY})
    public void verifySaveWhenEstateRequestIsNullAndExpect400() throws Exception {
        mockMvc.perform(
                post(ESTATES_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(EMPTY_JSON))
                .andExpect(status().isBadRequest());

        verify(modelMapper, never()).map(any(EstateRequest.class), eq(Estate.class));
        verify(estateService, never()).save(any(Estate.class));
        verify(modelMapper, never()).map(any(Estate.class), eq(EstateResponse.class));
    }

    @Test
    @WithMockUser(authorities = Permission.Value.BUYER)
    public void verifySaveWhenUserIsNotAuthorizedAndExpect403() throws Exception {
        final String jsonRequest = loadJsonResource(ESTATE_REQUEST_JSON_PATH, EstateRequest.class);

        mockMvc.perform(
                post(ESTATES_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isForbidden());

        verify(modelMapper, never()).map(any(EstateRequest.class), eq(Estate.class));
        verify(estateService, never()).save(any(Estate.class));
        verify(modelMapper, never()).map(any(Estate.class), eq(EstateResponse.class));
    }

    @Test
    public void verifySaveWhenUserIsNotAuthenticatedAndExpect401() throws Exception {
        final String jsonRequest = loadJsonResource(ESTATE_REQUEST_JSON_PATH, EstateRequest.class);

        mockMvc.perform(
                post(ESTATES_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUnauthorized());

        verify(modelMapper, never()).map(any(EstateRequest.class), eq(Estate.class));
        verify(estateService, never()).save(any(Estate.class));
        verify(modelMapper, never()).map(any(Estate.class), eq(EstateResponse.class));
    }

    @Test
    @WithMockUser(authorities = {Permission.Value.ADMIN, Permission.Value.AGENCY})
    public void verifyUpdateAndExpect200() throws Exception {
        final String jsonRequest = loadJsonResource(ESTATE_REQUEST_JSON_PATH, EstateRequest.class);
        final String jsonResponse = loadJsonResource(ESTATE_RESPONSE_JSON_PATH, EstateResponse.class);

        when(estateService.update(eq(ID), any(Estate.class))).thenReturn(estate);

        mockMvc.perform(
                put(ESTATES_BASE_URL + "/" + ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonResponse, true));

        verify(modelMapper, times(1)).map(estateRequest, Estate.class);
        verify(estateService, times(1)).update(eq(ID), any(Estate.class));
        verify(modelMapper, times(1)).map(estate, EstateResponse.class);
    }

    @Test
    @WithMockUser(authorities = {Permission.Value.ADMIN, Permission.Value.AGENCY})
    public void verifyUpdateWhenEstateRequestIsNotValidAndExpect404() throws Exception {
        final String jsonRequest = loadJsonResource(ESTATE_REQUEST_JSON_PATH, EstateRequest.class);

        when(estateService.update(eq(ID), any(Estate.class))).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(
                put(ESTATES_BASE_URL + "/" + ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound());

        verify(modelMapper, times(1)).map(any(EstateRequest.class), eq(Estate.class));
        verify(estateService, times(1)).update(eq(ID), any(Estate.class));
        verify(modelMapper, never()).map(any(Estate.class), eq(EstateResponse.class));
    }

    @Test
    @WithMockUser(authorities = {Permission.Value.ADMIN, Permission.Value.AGENCY})
    public void verifyUpdateWhenEstateRequestIsNullAndExpect400() throws Exception {
        mockMvc.perform(
                put(ESTATES_BASE_URL + "/" + ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(EMPTY_JSON))
                .andExpect(status().isBadRequest());

        verify(modelMapper, never()).map(any(EstateRequest.class), eq(Estate.class));
        verify(estateService, never()).update(eq(ID), any(Estate.class));
        verify(modelMapper, never()).map(any(Estate.class), eq(EstateResponse.class));
    }

    @Test
    @WithMockUser(authorities = Permission.Value.BUYER)
    public void verifyUpdateWhenUserIsNotAuthorizedAndExpect403() throws Exception {
        final String jsonRequest = loadJsonResource(ESTATE_REQUEST_JSON_PATH, EstateRequest.class);

        mockMvc.perform(
                put(ESTATES_BASE_URL + "/" + ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isForbidden());

        verify(modelMapper, never()).map(any(EstateRequest.class), eq(Estate.class));
        verify(estateService, never()).update(eq(ID), any(Estate.class));
        verify(modelMapper, never()).map(any(Estate.class), eq(EstateResponse.class));
    }

    @Test
    public void verifyUpdateWhenUserIsNotAuthenticatedAndExpect401() throws Exception {
        final String jsonRequest = loadJsonResource(ESTATE_REQUEST_JSON_PATH, EstateRequest.class);

        mockMvc.perform(
                put(ESTATES_BASE_URL + "/" + ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUnauthorized());

        verify(modelMapper, never()).map(any(EstateRequest.class), eq(Estate.class));
        verify(estateService, never()).update(eq(ID), any(Estate.class));
        verify(modelMapper, never()).map(any(Estate.class), eq(EstateResponse.class));
    }
}
