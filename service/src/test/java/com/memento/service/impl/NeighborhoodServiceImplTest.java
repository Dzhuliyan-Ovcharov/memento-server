package com.memento.service.impl;

import com.memento.repository.NeighborhoodRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NeighborhoodServiceImplTest {

    private static final String CITY_NAME = "City name";

    @Mock
    private NeighborhoodRepository neighborhoodRepository;

    @InjectMocks
    private NeighborhoodServiceImpl neighborhoodService;

    @Test
    public void verifyFindAllByCityName() {
        when(neighborhoodRepository.findAllByCity_Name(CITY_NAME)).thenReturn(Collections.emptyList());

        neighborhoodService.findAllByCityName(CITY_NAME);

        verify(neighborhoodRepository, times(1)).findAllByCity_Name(CITY_NAME);
    }

    @Test(expected = NullPointerException.class)
    public void verifyFindAllByCityNameThrowsWhenCityNameIsNull() {
        neighborhoodService.findAllByCityName(null);
    }
}
