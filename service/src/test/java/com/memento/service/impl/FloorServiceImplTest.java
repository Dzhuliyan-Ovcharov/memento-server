package com.memento.service.impl;

import com.memento.model.Floor;
import com.memento.repository.FloorRepository;
import com.memento.shared.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FloorServiceImplTest {

    @Mock
    private FloorRepository floorRepository;

    @InjectMocks
    private FloorServiceImpl floorService;

    private Floor floor;

    @Before
    public void setUp() {
        floor = mock(Floor.class);
    }

    @Test
    public void verifyGetAllFloors() {
        when(floorRepository.findAll()).thenReturn(Collections.emptyList());

        floorService.getAll();

        verify(floorRepository, times(1)).findAll();
    }

    @Test
    public void verifySaveWhenFloorIsNotNull() {
        when(floorRepository.save(any(Floor.class))).thenReturn(floor);

        floorService.save(floor);

        verify(floorRepository, times(1)).save(any(Floor.class));
    }

    @Test(expected = NullPointerException.class)
    public void throwsWhenFloorIsNull() {
        floorService.save(null);
    }

    @Test
    public void verifyFindByNumberWhenFloorIsFound() {
        when(floorRepository.findFloorByNumber(anyInt())).thenReturn(Optional.of(floor));

        floorService.findByNumber(0);

        verify(floorRepository, times(1)).findFloorByNumber(anyInt());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void throwsWhenFloorIsNotFound() {
        when(floorRepository.findFloorByNumber(anyInt())).thenReturn(Optional.empty());

        floorService.findByNumber(0);

        verify(floorRepository, times(1)).findFloorByNumber(anyInt());
    }
}