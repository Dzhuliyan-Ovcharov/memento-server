package com.memento.service.impl;

import com.memento.model.Image;
import com.memento.repository.ImageRepository;
import com.memento.shared.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.ResourceLoader;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ImageServiceImplTest {

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ResourceLoader resourceLoader;

    @InjectMocks
    private ImageServiceImpl imageService;

    private Image image;

    @Before
    public void setUp() {
        image = mock(Image.class);
    }

    @Test
    public void verifyFindOneImageWhenImageIsFound() {
        when(imageRepository.findByPath(anyString())).thenReturn(Optional.of(image));

        imageService.findOneImage("");

        verify(imageRepository, times(1)).findByPath(anyString());
        verify(image, times(1)).getPath();
        verify(resourceLoader, times(1)).getResource(anyString());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void throwsWhenImageIsNotFound() {
        when(imageRepository.findByPath(anyString())).thenReturn(Optional.empty());

        imageService.findOneImage("");

        verify(imageRepository, times(1)).findByPath(anyString());
    }
}