package com.memento.service.impl;

import com.memento.model.City;
import com.memento.repository.CityRepository;
import com.memento.shared.exception.BadRequestException;
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
    public void verifyGetAllCities() {
        when(cityRepository.findAll()).thenReturn(Collections.emptyList());

        cityService.getAll();

        verify(cityRepository, times(1)).findAll();
    }

    @Test
    public void verifySaveWhenCityIsNotNull() {
        City city = mock(City.class);
        when(cityRepository.save(any(City.class))).thenReturn(city);

        cityService.save(city);

        verify(cityRepository, times(1)).save(any(City.class));
    }

    @Test
    public void verifyUpdateWhenCityIdIsFound() {
        City city = mock(City.class);
        City oldCity = mock(City.class);
        City newCity = mock(City.class);

        when(cityRepository.findById(anyLong())).thenReturn(Optional.of(oldCity));
        when(cityRepository.save(any(City.class))).thenReturn(newCity);

        cityService.update(city.getId(), city);

        verify(city, times(2)).getId();
        verify(cityRepository, times(1)).findById(anyLong());
        verify(oldCity, times(1)).getNeighborhoods();
        verify(oldCity, times(1)).getId();
        verify(city, times(1)).getName();
        verify(city, times(1)).getNeighborhoods();
        verify(cityRepository, times(1)).save(any(City.class));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void throwsWhenCityIdIsNotFound() {
        City city = mock(City.class);

        when(cityRepository.findById(anyLong())).thenReturn(Optional.empty());

        cityService.update(city.getId(), city);
    }

    @Test(expected = BadRequestException.class)
    public void throwsWhenCityIdDoesNotMatchWithPassedId() {
        City city = mock(City.class);

        cityService.update(1L, city);
        verify(city, times(1)).getId();
    }
}