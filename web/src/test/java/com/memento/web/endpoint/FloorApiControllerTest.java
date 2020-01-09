package com.memento.web.endpoint;

import com.memento.model.Floor;
import com.memento.service.FloorService;
import com.memento.shared.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.Collections;

import static com.memento.web.RequestUrlConstant.FLOORS_BASE_URL;
import static com.memento.web.constant.JsonPathConstant.FLOOR_COLLECTION_JSON_PATH;
import static com.memento.web.constant.JsonPathConstant.FLOOR_JSON_PATH;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FloorApiController.class)
public class FloorApiControllerTest extends BaseApiControllerTest {

    private static final Long ID = 1L;
    private static final Integer NUMBER = 1;

    private Floor floor;

    @MockBean
    private FloorService floorService;

    @Before
    public void init() {
        floor = Floor.builder()
                .id(ID)
                .number(NUMBER)
                .build();
    }

    @Test
    public void verifyGetAllFloorsAndExpect200() throws Exception {
        final String jsonResponse = loadJsonResource(FLOOR_COLLECTION_JSON_PATH, Floor[].class);

        when(floorService.getAll()).thenReturn(Collections.singleton(floor));

        mockMvc.perform(
                get(FLOORS_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonResponse, true));

        verify(floorService, times(1)).getAll();
    }

    @Test
    public void verifyGetAllFloorsWhenFloorsAreNotPresentAndExpect200() throws Exception {
        when(floorService.getAll()).thenReturn(Collections.emptySet());

        mockMvc.perform(
                get(FLOORS_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(EMPTY_JSON_COLLECTION, true));

        verify(floorService, times(1)).getAll();
    }

    @Test
    public void verifyFindByNumberWhenNumberIsFoundAndExpect200() throws Exception {
        final String jsonResponse = loadJsonResource(FLOOR_JSON_PATH, Floor.class);

        when(floorService.findByNumber(NUMBER)).thenReturn(floor);

        mockMvc.perform(
                get(FLOORS_BASE_URL + "/" + NUMBER)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonResponse, true));

        verify(floorService, times(1)).findByNumber(NUMBER);
    }

    @Test
    public void verifyFindByNumberWhenNumberIsNotFoundAndExpect404() throws Exception {
        when(floorService.findByNumber(anyInt())).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(
                get(FLOORS_BASE_URL + "/" + NUMBER)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(floorService, times(1)).findByNumber(NUMBER);
    }

    @Test
    public void verifySaveWhenFloorIsNotNullAndExpect200() throws Exception {
        final String jsonRequest = loadJsonResource(FLOOR_JSON_PATH, Floor.class);

        when(floorService.save(floor)).thenReturn(floor);

        mockMvc.perform(
                post(FLOORS_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonRequest, true));

        verify(floorService, times(1)).save(floor);
    }

    @Test
    public void verifySaveWhenFloorIsNullAndExpect400() throws Exception {
        mockMvc.perform(
                post(FLOORS_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(EMPTY_JSON))
                .andExpect(status().isBadRequest());

        verify(floorService, never()).save(any(Floor.class));
    }
}
