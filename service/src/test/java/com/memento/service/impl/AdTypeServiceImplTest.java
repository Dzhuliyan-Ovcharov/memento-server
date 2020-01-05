package com.memento.service.impl;

import com.memento.model.AdType;
import com.memento.repository.AdTypeRepository;
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
public class AdTypeServiceImplTest {

    private AdType adType;

    @Mock
    private AdTypeRepository adTypeRepository;

    @InjectMocks
    private AdTypeServiceImpl adTypeService;

    @Before
    public void setUp() {
        adType = mock(AdType.class);
    }

    @Test
    public void verifyFindByType() {
        when(adTypeRepository.findAdTypeByType(anyString())).thenReturn(Optional.of(adType));

        adTypeService.findByType(StringUtils.EMPTY);

        verify(adTypeRepository, times(1)).findAdTypeByType(anyString());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void verifyFindByTypeThrowsWhenTypeIsNotFound() {
        when(adTypeRepository.findAdTypeByType(anyString())).thenReturn(Optional.empty());

        adTypeService.findByType(StringUtils.EMPTY);
    }

    @Test
    public void verifyGetAllAdTypes() {
        when(adTypeRepository.findAll()).thenReturn(Collections.emptyList());

        adTypeService.getAll();

        verify(adTypeRepository, times(1)).findAll();
    }

    @Test
    public void verifySaveWhenAdTypeIsNotNull() {
        when(adTypeRepository.save(any(AdType.class))).thenReturn(adType);

        adTypeService.save(adType);

        verify(adTypeRepository, times(1)).save(any(AdType.class));
    }
}
