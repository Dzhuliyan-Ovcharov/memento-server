package com.memento.service.impl;

import com.memento.repository.NeighborhoodRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NeighborhoodServiceImplTest {

    @Mock
    private NeighborhoodRepository neighborhoodRepository;

    @InjectMocks
    private NeighborhoodServiceImpl neighborhoodService;

    @Test
    public void findAllByCityName_whenNeighborhoodsAreFound_expectTheNeighborhoods() {
        when(neighborhoodRepository.findAllByCity_Name(anyString())).thenReturn(Collections.emptyList());

        neighborhoodService.findAllByCityName("");

        verify(neighborhoodRepository, times(1)).findAllByCity_Name(anyString());
    }
}