package com.memento.service.impl;

import com.memento.model.EstateType;
import com.memento.repository.EstateTypeRepository;
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
public class EstateTypeServiceImplTest {

    private static final Long ID = 1L;
    private static final String TYPE = "Estate type";

    private EstateType estateType;

    @Mock
    private EstateTypeRepository estateTypeRepository;

    @InjectMocks
    private EstateTypeServiceImpl estateTypeService;

    @Before
    public void setUp() {
        estateType = mock(EstateType.class);
        when(estateType.getId()).thenReturn(ID);
    }

    @Test
    public void verifyFindByType() {
        when(estateTypeRepository.findEstateTypeByType(anyString())).thenReturn(Optional.of(estateType));

        estateTypeService.findByType(TYPE);

        verify(estateTypeRepository, times(1)).findEstateTypeByType(TYPE);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void verifyFindByTypeThrowsWhenTypeIsNotFound() {
        when(estateTypeRepository.findEstateTypeByType(anyString())).thenReturn(Optional.empty());

        estateTypeService.findByType(TYPE);
    }

    @Test(expected = NullPointerException.class)
    public void verifyFindByTypeThrowsWhenTypeIsNull() {
        estateTypeService.findByType(null);
    }

    @Test
    public void verifyGetAll() {
        when(estateTypeRepository.findAll()).thenReturn(Collections.emptyList());

        estateTypeService.getAll();

        verify(estateTypeRepository, times(1)).findAll();
    }

    @Test
    public void verifySave() {
        when(estateTypeRepository.save(any(EstateType.class))).thenReturn(estateType);

        estateTypeService.save(estateType);

        verify(estateTypeRepository, times(1)).save(estateType);
    }

    @Test(expected = NullPointerException.class)
    public void verifySaveThrowsWhenEstateTypeIsNull() {
        estateTypeService.save(null);
    }

    @Test
    public void verifyUpdate() {
        final EstateType oldEstateType = mock(EstateType.class);
        final EstateType newEstateType = mock(EstateType.class);

        when(estateTypeRepository.findById(anyLong())).thenReturn(Optional.of(oldEstateType));
        when(estateTypeRepository.save(any(EstateType.class))).thenReturn(newEstateType);

        estateTypeService.update(ID, estateType);

        verify(estateType, times(2)).getId();
        verify(estateTypeRepository, times(1)).findById(ID);
        verify(oldEstateType, times(1)).getId();
        verify(estateType, times(1)).getType();
        verify(oldEstateType, times(1)).getEstates();
        verify(estateTypeRepository, times(1)).save(any(EstateType.class));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void verifyUpdateThrowsWhenEstateTypeIdIsNotFound() {
        when(estateTypeRepository.findById(anyLong())).thenReturn(Optional.empty());

        estateTypeService.update(ID, estateType);
    }

    @Test(expected = NullPointerException.class)
    public void verifyUpdateThrowsWhenIdIsNull() {
        estateTypeService.update(null, estateType);
    }

    @Test(expected = NullPointerException.class)
    public void verifyUpdateThrowsWhenEstateTypeIsNull() {
        estateTypeService.update(ID, null);
    }
}
