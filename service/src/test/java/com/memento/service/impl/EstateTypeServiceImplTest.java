package com.memento.service.impl;

import com.memento.model.EstateType;
import com.memento.repository.EstateTypeRepository;
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
public class EstateTypeServiceImplTest {

    @Mock
    private EstateTypeRepository estateTypeRepository;

    @InjectMocks
    private EstateTypeServiceImpl estateTypeService;

    @Test
    public void verifyGetAllEstateTypes() {
        when(estateTypeRepository.findAll()).thenReturn(Collections.emptyList());

        estateTypeService.getAll();

        verify(estateTypeRepository, times(1)).findAll();
    }

    @Test
    public void verifySaveWhenEstateTypeIsNotNull() {
        EstateType estateType = mock(EstateType.class);
        when(estateTypeRepository.save(any(EstateType.class))).thenReturn(estateType);

        estateTypeService.save(estateType);

        verify(estateTypeRepository, times(1)).save(any(EstateType.class));
    }

    @Test
    public void verifyUpdateWhenEstateTypeIdIsFound() {
        EstateType estateType = mock(EstateType.class);
        EstateType oldEstateType = mock(EstateType.class);
        EstateType newEstateType = mock(EstateType.class);

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
    public void throwsWhenEstateTypeIdIsNotFound() {
        EstateType newEstateType = mock(EstateType.class);

        when(estateTypeRepository.findById(anyLong())).thenReturn(Optional.empty());

        estateTypeService.update(newEstateType);
    }
}