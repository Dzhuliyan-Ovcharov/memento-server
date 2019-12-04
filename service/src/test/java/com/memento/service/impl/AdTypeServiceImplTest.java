package com.memento.service.impl;

import com.memento.model.AdType;
import com.memento.repository.AdTypeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AdTypeServiceImplTest {

    @Mock
    private AdTypeRepository adTypeRepository;

    @InjectMocks
    private AdTypeServiceImpl adTypeService;

    @Test
    public void getAll_whenAdTypesAreAvailable_expectTheAdTypes() {
        when(adTypeRepository.findAll()).thenReturn(Collections.emptyList());

        adTypeService.getAll();

        verify(adTypeRepository, times(1)).findAll();
    }

    @Test
    public void save_whenAdTypeIsNotNull_expectToSave() {
        AdType adType = mock(AdType.class);
        when(adTypeRepository.save(any(AdType.class))).thenReturn(adType);

        adTypeService.save(adType);

        verify(adTypeRepository, times(1)).save(any(AdType.class));
    }
}