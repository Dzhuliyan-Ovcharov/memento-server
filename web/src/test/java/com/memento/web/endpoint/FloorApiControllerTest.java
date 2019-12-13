package com.memento.web.endpoint;

import com.memento.model.Floor;
import com.memento.service.impl.FloorServiceImpl;
import com.memento.shared.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FloorApiController.class)
public class FloorApiControllerTest extends BaseApiControllerTest {

    @MockBean
    private FloorServiceImpl floorService;

    private Floor floor;

    @Before
    public void init() {
        floor = Floor.builder()
                .id(1L)
                .number(2)
                .build();
    }

    @Test
    public void verifyGetAllFloorsAndExpect200() throws Exception {
        final Set<Floor> floors = Collections.singleton(floor);

        when(floorService.getAll()).thenReturn(floors);

        mockMvc.perform(
                get("/api/floor/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$.[0].length()", is(2)))
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
        when(floorService.findByNumber(anyInt())).thenReturn(floor);

        mockMvc.perform(
                get("/api/floor/number/0")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)))
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
        final String jsonString = objectMapper.writeValueAsString(floor);

        when(floorService.save(any(Floor.class))).thenReturn(floor);

        mockMvc.perform(
                post("/api/floor/save")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.number", is(2)));

        verify(floorService, times(1)).save(any(Floor.class));
    }

    @Test
    public void verifySaveWhenFloorIsNullAndExpect400() throws Exception {
        final String jsonString = objectMapper.writeValueAsString(null);

        mockMvc.perform(
                post("/api/floor/save")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isBadRequest());

        verify(floorService, never()).save(any(Floor.class));
    }

}
