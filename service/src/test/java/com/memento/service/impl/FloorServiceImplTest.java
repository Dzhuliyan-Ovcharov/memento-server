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

    private static final Integer NUMBER = 1;

    private Floor floor;

    @Mock
    private FloorRepository floorRepository;

    @InjectMocks
    private FloorServiceImpl floorService;

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
    public void verifySave() {
        when(floorRepository.save(floor)).thenReturn(floor);

        floorService.save(floor);

        verify(floorRepository, times(1)).save(floor);
    }

    @Test(expected = NullPointerException.class)
    public void verifySaveThrowsWhenFloorIsNull() {
        floorService.save(null);
    }

    @Test
    public void verifyFindByNumber() {
        when(floorRepository.findFloorByNumber(NUMBER)).thenReturn(Optional.of(floor));

        floorService.findByNumber(NUMBER);

        verify(floorRepository, times(1)).findFloorByNumber(NUMBER);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void verifyFindByNumberThrowsWhenFloorIsNotFound() {
        when(floorRepository.findFloorByNumber(anyInt())).thenReturn(Optional.empty());

        floorService.findByNumber(NUMBER);

        verify(floorRepository, times(1)).findFloorByNumber(NUMBER);
    }

    @Test(expected = NullPointerException.class)
    public void verifyFindByNumberWhenNumberIsNull() {
        floorService.findByNumber(null);
    }
}
