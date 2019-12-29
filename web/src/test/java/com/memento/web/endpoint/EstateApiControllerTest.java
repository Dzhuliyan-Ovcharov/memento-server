package com.memento.web.endpoint;

import com.memento.model.*;
import com.memento.service.EstateService;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EstateApiController.class)
public class EstateApiControllerTest extends BaseApiControllerTest {

    @MockBean
    private EstateService estateService;

    private Estate estate;

    @Before
    public void init() {
        final Floor floor = Floor.builder()
                .id(2L)
                .number(8)
                .build();

        final EstateType estateType = EstateType.builder()
                .id(3L)
                .type("EstateType")
                .estates(Collections.emptySet())
                .build();

        final AdType adType = AdType.builder()
                .id(4L)
                .type("AdType")
                .estates(Collections.emptySet())
                .build();

        final User user = User.builder()
                .id(6L)
                .firstName("First name")
                .lastName("Last name")
                .email("email@email.email")
                .password("password")
                .role(Role.builder().id(7L).permission(Permission.BUYER).build())
                .estates(Collections.emptyList())
                .build();

        final Image image = Image.builder()
                .id(8L)
                .name("Image name")
                .build();

        estate = Estate.builder()
                .id(1L)
                .price(Money.of(CurrencyUnit.USD, BigDecimal.valueOf(10000)))
                .quadrature(Quadrature.of(BigDecimal.valueOf(100), BigDecimal.valueOf(50)))
                .description("Description")
                .floor(floor)
                .estateType(estateType)
                .adType(adType)
                .user(user)
                .images(Collections.singletonList(image))
                .build();
    }

    @Test
    @WithMockUser(authorities = {Permission.Value.ADMIN, Permission.Value.AGENCY})
    public void verifySaveAndExpect200() throws Exception {
        final String jsonString = objectMapper.writeValueAsString(estate);

        when(estateService.save(any(Estate.class))).thenReturn(estate);

        mockMvc.perform(
                post("/api/estate/save")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.*", hasSize(9)))
                .andExpect(jsonPath("$.id", is(1)))

                .andExpect(jsonPath("$.price.*", hasSize(2)))
                .andExpect(jsonPath("$.price.amount", is(10000.00)))
                .andExpect(jsonPath("$.price.currency", is("USD")))

                .andExpect(jsonPath("$.quadrature.*", hasSize(3)))
                .andExpect(jsonPath("$.quadrature.builtUpArea", is(100)))
                .andExpect(jsonPath("$.quadrature.pureArea", is(50)))
                .andExpect(jsonPath("$.quadrature.difference", is(50)))

                .andExpect(jsonPath("$.description", is("Description")))

                .andExpect(jsonPath("$.floor.*", hasSize(2)))
                .andExpect(jsonPath("$.floor.id", is(2)))
                .andExpect(jsonPath("$.floor.number", is(8)))

                .andExpect(jsonPath("$.estateType.*", hasSize(3)))
                .andExpect(jsonPath("$.estateType.id", is(3)))
                .andExpect(jsonPath("$.estateType.type", is("EstateType")))
                .andExpect(jsonPath("$.estateType.estates", is(empty())))

                .andExpect(jsonPath("$.adType.*", hasSize(3)))
                .andExpect(jsonPath("$.adType.id", is(4)))
                .andExpect(jsonPath("$.adType.type", is("AdType")))
                .andExpect(jsonPath("$.adType.estates", is(empty())))

                .andExpect(jsonPath("$.user.*", hasSize(7)))
                .andExpect(jsonPath("$.user.id", is(6)))
                .andExpect(jsonPath("$.user.firstName", is("First name")))
                .andExpect(jsonPath("$.user.lastName", is("Last name")))
                .andExpect(jsonPath("$.user.email", is("email@email.email")))
                .andExpect(jsonPath("$.user.password", is("password")))
                .andExpect(jsonPath("$.user.role.*", hasSize(3)))
                .andExpect(jsonPath("$.user.role.id", is(7)))
                .andExpect(jsonPath("$.user.role.permission", is("BUYER")))
                .andExpect(jsonPath("$.user.role.authority", is("BUYER")))
                .andExpect(jsonPath("$.user.estates.*", is(empty())))

                .andExpect(jsonPath("$.images.*", hasSize(1)))
                .andExpect(jsonPath("$.images[0].*", hasSize(3)))
                .andExpect(jsonPath("$.images[0].id", is(8)))
                .andExpect(jsonPath("$.images[0].name", is("Image name")))
                .andExpect(jsonPath("$.images[0].estate", is(nullValue())));

        verify(estateService, times(1)).save(any(Estate.class));
    }

    @Test
    @WithMockUser(authorities = {Permission.Value.ADMIN, Permission.Value.AGENCY})
    public void verifySaveWhenEstateIsNullAndExpect400() throws Exception {
        final String jsonString = objectMapper.writeValueAsString(null);

        mockMvc.perform(
                post("/api/estate/save")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isBadRequest());

        verify(estateService, never()).save(any(Estate.class));
    }

    @Test
    @WithMockUser(authorities = Permission.Value.BUYER)
    public void verifySaveWhenUserIsNotAuthorizedAndExpect403() throws Exception {
        final String jsonString = objectMapper.writeValueAsString(estate);

        mockMvc.perform(
                post("/api/estate/save")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isForbidden());

        verify(estateService, never()).save(any(Estate.class));
    }

    @Test
    public void verifySaveWhenUserIsNotAuthenticatedAndExpect401() throws Exception {
        final String jsonString = objectMapper.writeValueAsString(estate);

        mockMvc.perform(
                post("/api/estate/save")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isUnauthorized());

        verify(estateService, never()).save(any(Estate.class));
    }
}
