package com.memento.service.impl;

import com.memento.model.City;
import com.memento.repository.CityRepository;
import com.memento.shared.exception.ResourceNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CityServiceImplTest {

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private CityServiceImpl cityService;

    @Test
    public void getAllTest() {
        when(cityRepository.findAll()).thenReturn(Collections.emptyList());
        cityService.getAll();
        verify(cityRepository).findAll();
    }

    @Test
    public void saveTest() {
        City city = mock(City.class);
        when(cityRepository.save(any(City.class))).thenReturn(city);
        cityService.save(city);
        verify(cityRepository).save(any(City.class));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void updateTest() {
        City city = mock(City.class);
        City oldCity = mock(City.class);
        City newCity = mock(City.class);

        when(cityRepository.findById(anyLong())).thenReturn(Optional.of(oldCity));
        when(cityRepository.save(any(City.class))).thenReturn(newCity);
        cityService.update(city);
        verify(city).getId();
        verify(oldCity).getId();
        verify(city).getName();
        verify(city).getNeighborhoods();
        verify(oldCity).getNeighborhoods();
        verify(cityRepository).findById(anyLong());
        verify(cityRepository).save(any(City.class));

        when(city.getId()).thenReturn(2L);
        when(cityRepository.findById(2L)).thenReturn(Optional.empty());
        cityService.update(city);
    }
}