package com.memento.service.impl;

import com.memento.model.EstateType;
import com.memento.repository.EstateTypeRepository;
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
public class EstateTypeServiceImplTest {

    private EstateType estateType;

    @Mock
    private EstateTypeRepository estateTypeRepository;

    @InjectMocks
    private EstateTypeServiceImpl estateTypeService;

    @Before
    public void setUp() {
        estateType = mock(EstateType.class);
    }

    @Test
    public void verifyFindByType() {
        when(estateTypeRepository.findEstateTypeByType(anyString())).thenReturn(Optional.of(estateType));

        estateTypeService.findByType(StringUtils.EMPTY);

        verify(estateTypeRepository, times(1)).findEstateTypeByType(anyString());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void verifyFindByTypeThrowsWhenTypeIsNotFound() {
        when(estateTypeRepository.findEstateTypeByType(anyString())).thenReturn(Optional.empty());

        estateTypeService.findByType(StringUtils.EMPTY);
    }

    @Test
    public void verifyGetAllEstateTypes() {
        when(estateTypeRepository.findAll()).thenReturn(Collections.emptyList());

        estateTypeService.getAll();

        verify(estateTypeRepository, times(1)).findAll();
    }

    @Test
    public void verifySaveWhenEstateTypeIsNotNull() {
        when(estateTypeRepository.save(any(EstateType.class))).thenReturn(estateType);

        estateTypeService.save(estateType);

        verify(estateTypeRepository, times(1)).save(any(EstateType.class));
    }

    @Test
    public void verifyUpdateWhenEstateTypeIdIsFound() {
        final EstateType oldEstateType = mock(EstateType.class);
        final EstateType newEstateType = mock(EstateType.class);

        when(estateTypeRepository.findById(anyLong())).thenReturn(Optional.of(oldEstateType));
        when(estateTypeRepository.save(any(EstateType.class))).thenReturn(newEstateType);

        estateTypeService.update(estateType);

        verify(estateType, times(1)).getId();
        verify(estateTypeRepository, times(1)).findById(anyLong());
        verify(oldEstateType, times(1)).getId();
        verify(estateType, times(1)).getType();
        verify(oldEstateType, times(1)).getEstates();
        verify(estateTypeRepository, times(1)).save(any(EstateType.class));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void verifyUpdateThrowsWhenEstateTypeIdIsNotFound() {
        when(estateTypeRepository.findById(anyLong())).thenReturn(Optional.empty());

        estateTypeService.update(estateType);
    }
}
