package com.memento.service.impl;

import com.memento.model.*;
import com.memento.repository.EstateRepository;
import com.memento.service.AdTypeService;
import com.memento.service.EstateTypeService;
import com.memento.service.FloorService;
import com.memento.service.UserService;
import com.memento.shared.exception.ResourceNotFoundException;
import org.apache.commons.lang3.StringUtils;
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
public class EstateServiceImplTest {

    private static final Long id = 1L;

    private Estate estate;

    private Floor floor;

    private EstateType estateType;

    private AdType adType;

    private User user;

    @Mock
    private FloorService floorService;

    @Mock
    private EstateTypeService estateTypeService;

    @Mock
    private AdTypeService adTypeService;

    @Mock
    private UserService userService;

    @Mock
    private EstateRepository estateRepository;

    @InjectMocks
    private EstateServiceImpl estateService;

    @Before
    public void setUp() {
        floor = mock(Floor.class);
        estateType = mock(EstateType.class);
        adType = mock(AdType.class);
        user = mock(User.class);
        estate = mock(Estate.class);

        when(estate.getFloor()).thenReturn(floor);
        when(estate.getEstateType()).thenReturn(estateType);
        when(estate.getAdType()).thenReturn(adType);
        when(estate.getUser()).thenReturn(user);
    }

    @Test
    public void verifyFindById() {
        when(estateRepository.findById(anyLong())).thenReturn(Optional.of(estate));

        estateService.findById(id);

        verify(estateRepository, times(1)).findById(anyLong());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void verifyFindByIdThrowsWhenIdIsNotFound() {
        when(estateRepository.findById(anyLong())).thenReturn(Optional.empty());

        estateService.findById(id);
    }

    @Test
    public void verifyGetAll() {
        when(estateRepository.findAll()).thenReturn(Collections.singletonList(estate));

        estateService.getAll();

        verify(estateRepository, times(1)).findAll();
    }

    @Test
    public void verifySave() {
        final Floor floorToSave = mock(Floor.class);
        final EstateType estateTypeToSave = mock(EstateType.class);
        final AdType adTypeToSave = mock(AdType.class);
        final User userToSave = mock(User.class);
        final Estate estateToSave = mock(Estate.class);

        when(floor.getNumber()).thenReturn(1);
        when(floorService.findByNumber(anyInt())).thenReturn(floorToSave);
        when(estateType.getType()).thenReturn(StringUtils.EMPTY);
        when(estateTypeService.findByType(anyString())).thenReturn(estateTypeToSave);
        when(adType.getType()).thenReturn(StringUtils.EMPTY);
        when(adTypeService.findByType(anyString())).thenReturn(adTypeToSave);
        when(user.getEmail()).thenReturn(StringUtils.EMPTY);
        when(userService.findByEmail(anyString())).thenReturn(userToSave);
        when(estateRepository.save(any(Estate.class))).thenReturn(estateToSave);

        estateService.save(estate);

        verify(estate, times(1)).getFloor();
        verify(floor, times(1)).getNumber();
        verify(floorService, times(1)).findByNumber(anyInt());
        verify(estate, times(1)).getEstateType();
        verify(estateType, times(1)).getType();
        verify(estateTypeService, times(1)).findByType(anyString());
        verify(estate, times(1)).getAdType();
        verify(adType, times(1)).getType();
        verify(adTypeService, times(1)).findByType(anyString());
        verify(estate, times(1)).getUser();
        verify(user, times(1)).getEmail();
        verify(userService, times(1)).findByEmail(anyString());
        verify(estate, times(1)).getPrice();
        verify(estate, times(1)).getQuadrature();
        verify(estate, times(1)).getDescription();
        verify(estateRepository, times(1)).save(any(Estate.class));
    }

    @Test(expected = NullPointerException.class)
    public void verifySaveThrowsWhenEstateIsNull() {
        estateService.save(null);
    }

    @Test
    public void verifyUpdate() {
        final Floor floorToSave = mock(Floor.class);
        final EstateType estateTypeToSave = mock(EstateType.class);
        final AdType adTypeToSave = mock(AdType.class);
        final Estate oldEstate = mock(Estate.class);
        final Estate newEstate = mock(Estate.class);

        when(estateRepository.findById(anyLong())).thenReturn(Optional.of(oldEstate));
        when(floor.getNumber()).thenReturn(1);
        when(floorService.findByNumber(anyInt())).thenReturn(floorToSave);
        when(estateType.getType()).thenReturn(StringUtils.EMPTY);
        when(estateTypeService.findByType(anyString())).thenReturn(estateTypeToSave);
        when(adType.getType()).thenReturn(StringUtils.EMPTY);
        when(adTypeService.findByType(anyString())).thenReturn(adTypeToSave);
        when(estateRepository.save(any(Estate.class))).thenReturn(newEstate);

        estateService.update(id, estate);

        verify(estateRepository, times(1)).findById(anyLong());
        verify(estate, times(1)).getFloor();
        verify(floor, times(1)).getNumber();
        verify(floorService, times(1)).findByNumber(anyInt());
        verify(estate, times(1)).getEstateType();
        verify(estateType, times(1)).getType();
        verify(estateTypeService, times(1)).findByType(anyString());
        verify(estate, times(1)).getAdType();
        verify(adType, times(1)).getType();
        verify(adTypeService, times(1)).findByType(anyString());
        verify(oldEstate, times(1)).getId();
        verify(estate, times(1)).getPrice();
        verify(estate, times(1)).getQuadrature();
        verify(estate, times(1)).getDescription();
        verify(oldEstate, times(1)).getUser();
        verify(oldEstate, times(1)).getImages();
        verify(estateRepository, times(1)).save(any(Estate.class));
    }

    @Test(expected = NullPointerException.class)
    public void verifyUpdateThrowsWhenIdIsNull() {
        estateService.update(null, estate);
    }

    @Test(expected = NullPointerException.class)
    public void verifyUpdateThrowsWhenEstateIsNull() {
        estateService.update(id, null);
    }
}
