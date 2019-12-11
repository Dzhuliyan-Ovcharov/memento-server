package com.memento.web.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.memento.model.Floor;
import com.memento.service.configuration.security.SecurityConfigurationTest;
import com.memento.service.impl.FloorServiceImpl;
import com.memento.shared.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(value = FloorApiController.class)
@Import(SecurityConfigurationTest.class)
public class FloorApiControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FloorServiceImpl floorService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                //.apply(springSecurity())
                .alwaysDo(print())
                .build();
    }

    @Test
    public void verifyGetAllFloorsAndExpect200() throws Exception {
        final Set<Floor> floors = Collections.singleton(
                Floor.builder()
                        .id(1L)
                        .number(2)
                        .build());

        when(floorService.getAll()).thenReturn(floors);

        mockMvc.perform(
                get("/api/floor/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].number", is(2)));

        verify(floorService, times(1)).getAll();
    }

    @Test
    public void verifyGetAllFloorsWhenFloorsAreNotPresentAndExpect200() throws Exception {
        when(floorService.getAll()).thenReturn(Collections.emptySet());

        mockMvc.perform(
                get("/api/floor/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(0)));

        verify(floorService, times(1)).getAll();
    }

    @Test
    public void verifyFindByNumberWhenNumberIsFoundAndExpect200() throws Exception {
        final Floor floor = Floor.builder()
                .id(1L)
                .number(2)
                .build();
        when(floorService.findByNumber(anyInt())).thenReturn(floor);

        mockMvc.perform(
                get("/api/floor/number/0")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.number", is(2)));

        verify(floorService, times(1)).findByNumber(anyInt());
    }

    @Test
    public void verifyFindByNumberWhenNumberIsNotFoundAndExpect404() throws Exception {
        when(floorService.findByNumber(anyInt())).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(
                get("/api/floor/number/0")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(floorService, times(1)).findByNumber(anyInt());
    }

    @Test
    public void verifySaveWhenFloorIsNotNullAndExpect200() throws Exception {
        final Floor floor = Floor.builder()
                .id(1L)
                .number(2)
                .build();

        String jsonString = objectMapper.writeValueAsString(floor);

        when(floorService.save(any(Floor.class))).thenReturn(floor);

        mockMvc.perform(
                post("/api/floor/save")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.number", is(2)));

        verify(floorService, times(1)).save(any(Floor.class));
    }

    @Test
    public void verifySaveWhenFloorIsNullAndExpect400() throws Exception {
        final Floor floor = null;
        String jsonString = objectMapper.writeValueAsString(floor);

        mockMvc.perform(
                post("/api/floor/save")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isBadRequest());

        verify(floorService, never()).save(any(Floor.class));
    }

}