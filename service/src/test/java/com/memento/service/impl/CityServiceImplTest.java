package com.memento.service.impl;

import com.memento.model.City;
import com.memento.repository.CityRepository;
import com.memento.shared.exception.BadRequestException;
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
public class CityServiceImplTest {

    private static final Long ID = 1L;

    private City city;

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private CityServiceImpl cityService;

    @Before
    public void setUp() {
        city = mock(City.class);
    }

    @Test
    public void verifyGetAll() {
        when(cityRepository.findAll()).thenReturn(Collections.emptyList());

        cityService.getAll();

        verify(cityRepository, times(1)).findAll();
    }

    @Test
    public void verifySave() {
        when(cityRepository.save(city)).thenReturn(city);

        cityService.save(city);

        verify(cityRepository, times(1)).save(city);
    }

    @Test(expected = NullPointerException.class)
    public void verifySaveThrowsWhenCityIsNull() {
        cityService.save(null);
    }

    @Test
    public void verifyUpdate() {
        final City oldCity = mock(City.class);
        final City newCity = mock(City.class);

        when(city.getId()).thenReturn(ID);
        when(cityRepository.findById(ID)).thenReturn(Optional.of(oldCity));
        when(cityRepository.save(any(City.class))).thenReturn(newCity);

        cityService.update(city.getId(), city);

        verify(city, times(2)).getId();
        verify(cityRepository, times(1)).findById(ID);
        verify(oldCity, times(1)).getNeighborhoods();
        verify(oldCity, times(1)).getId();
        verify(city, times(1)).getName();
        verify(city, times(1)).getNeighborhoods();
        verify(cityRepository, times(1)).save(any(City.class));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void verifyUpdateThrowsWhenCityIdIsNotFound() {
        when(cityRepository.findById(anyLong())).thenReturn(Optional.empty());

        cityService.update(city.getId(), city);
    }

    @Test(expected = BadRequestException.class)
    public void verifyUpdateThrowsWhenCityIdDoesNotMatchWithPassedId() {
        cityService.update(ID, city);

        verify(city, times(1)).getId();
    }

    @Test(expected = NullPointerException.class)
    public void verifyUpdateThrowsWhenIdIsNull() {
        cityService.update(null, city);
    }

    @Test(expected = NullPointerException.class)
    public void verifyUpdateThrowsWhenCityIsNull() {
        cityService.update(ID, null);
    }

    @Test
    public void verifyFindById() {
        when(cityRepository.findById(ID)).thenReturn(Optional.of(city));

        cityService.findById(ID);

        verify(cityRepository, times(1)).findById(ID);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void verifyFindByIdThrowsWhenIdIsNotFound() {
        when(cityRepository.findById(anyLong())).thenReturn(Optional.empty());

        cityService.findById(ID);
    }

    @Test(expected = NullPointerException.class)
    public void verifyFindByIdThrowsWhenIdIsNull() {
        cityService.findById(null);
    }
}
