package com.memento.service.impl;

import com.memento.model.EstateType;
import com.memento.repository.EstateTypeRepository;
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
public class EstateTypeServiceImplTest {

    @Mock
    private EstateTypeRepository estateTypeRepository;

    @InjectMocks
    private EstateTypeServiceImpl estateTypeService;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void getAll_whenEstateTypesAreAvailable_expectTheEstateTypes() {
        when(estateTypeRepository.findAll()).thenReturn(Collections.emptyList());

        estateTypeService.getAll();

        verify(estateTypeRepository, times(1)).findAll();
    }

    @Test
    public void save_whenEstateTypeIsNotNull_expectToSave() {
        EstateType estateType = mock(EstateType.class);
        when(estateTypeRepository.save(any(EstateType.class))).thenReturn(estateType);

        estateTypeService.save(estateType);

        verify(estateTypeRepository, times(1)).save(any(EstateType.class));
    }

    @Test
    public void update_whenEstateTypeIdIsFound_expectTheUpdate() {
        EstateType estateType = mock(EstateType.class);
        EstateType oldEstateType = mock(EstateType.class);
        EstateType newEstateType = mock(EstateType.class);

        when(estateTypeRepository.findById(anyLong())).thenReturn(Optional.of(oldEstateType));
        when(estateTypeRepository.save(any(EstateType.class))).thenReturn(newEstateType);

        estateTypeService.update(estateType);

        InOrder inOrder = inOrder(estateTypeRepository, estateType, oldEstateType);
        inOrder.verify(estateType, times(1)).getId();
        inOrder.verify(estateTypeRepository, times(1)).findById(anyLong());
        inOrder.verify(oldEstateType, times(1)).getId();
        inOrder.verify(estateType, times(1)).getType();
        inOrder.verify(oldEstateType, times(1)).getEstates();
        inOrder.verify(estateTypeRepository, times(1)).save(any(EstateType.class));
    }

    @Test
    public void update_whenEstateTypeIdIsNotFound_expectToThrow() {
        EstateType newEstateType = mock(EstateType.class);

        when(estateTypeRepository.findById(anyLong())).thenReturn(Optional.empty());
        exception.expect(ResourceNotFoundException.class);

        estateTypeService.update(newEstateType);
    }
}