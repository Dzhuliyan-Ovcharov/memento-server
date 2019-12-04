package com.memento.service.impl;

import com.memento.model.City;
import com.memento.repository.CityRepository;
import com.memento.shared.exception.ResourceNotFoundException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
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

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void getAll_whenCitiesAreAvailable_expectTheCities() {
        when(cityRepository.findAll()).thenReturn(Collections.emptyList());

        cityService.getAll();

        verify(cityRepository, times(1)).findAll();
    }

    @Test
    public void save_whenCityIsNotNull_expectToSave() {
        City city = mock(City.class);
        when(cityRepository.save(any(City.class))).thenReturn(any(City.class));

        cityService.save(city);

        verify(cityRepository, times(1)).save(any(City.class));
    }

    @Test
    public void update_whenCityIdIsFound_expectTheUpdate() {
        City city = mock(City.class);
        City oldCity = mock(City.class);
        City newCity = mock(City.class);

        when(cityRepository.findById(anyLong())).thenReturn(Optional.of(oldCity));
        when(cityRepository.save(any(City.class))).thenReturn(newCity);

        cityService.update(city);

        InOrder inOrder = inOrder(cityRepository, city, oldCity);
        inOrder.verify(city, times(1)).getId();
        inOrder.verify(cityRepository, times(1)).findById(anyLong());
        inOrder.verify(oldCity, times(1)).getNeighborhoods();
        inOrder.verify(oldCity, times(1)).getId();
        inOrder.verify(city, times(1)).getName();
        inOrder.verify(city, times(1)).getNeighborhoods();
        inOrder.verify(cityRepository, times(1)).save(any(City.class));
    }

    @Test
    public void update_whenCityIdIsNotFound_expectToThrow() {
        City city = mock(City.class);

        when(cityRepository.findById(anyLong())).thenReturn(Optional.empty());
        exception.expect(ResourceNotFoundException.class);

        cityService.update(city);
    }
}