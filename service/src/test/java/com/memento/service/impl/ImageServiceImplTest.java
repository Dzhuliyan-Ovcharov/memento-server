package com.memento.service.impl;

import com.memento.model.Estate;
import com.memento.model.Image;
import com.memento.repository.EstateRepository;
import com.memento.repository.ImageRepository;
import com.memento.service.StorageService;
import com.memento.shared.exception.ResourceNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ImageServiceImplTest {

    @Mock
    private StorageService storageService;

    @Mock
    private EstateRepository estateRepository;

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageServiceImpl imageService;

    private Image image;

    @Before
    public void setUp() {
        image = mock(Image.class);
    }

    @Test
    public void verifyFindOneImageWhenImageIsFound() {
        final Resource resource = mock(Resource.class);
        when(imageRepository.findByName(anyString())).thenReturn(Optional.of(image));
        when(image.getName()).thenReturn(StringUtils.EMPTY);
        when(storageService.loadAsResource(anyString())).thenReturn(resource);

        imageService.findOneImage(StringUtils.EMPTY);

        verify(imageRepository, times(1)).findByName(anyString());
        verify(image, times(1)).getName();
        verify(storageService, times(1)).loadAsResource(anyString());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void verifyFindOneImageThrowsWhenImageIsNotFound() {
        when(imageRepository.findByName(anyString())).thenReturn(Optional.empty());

        imageService.findOneImage(StringUtils.EMPTY);

        verify(imageRepository, times(1)).findByName(anyString());
        verify(storageService, never()).loadAsResource(anyString());
    }

    @Test
    public void verifyGetAllImagesByEstateId() {
        final Resource resource = mock(Resource.class);
        when(imageRepository.findAllByEstateId(anyLong())).thenReturn(Collections.singletonList(image));
        when(image.getName()).thenReturn(StringUtils.EMPTY);
        when(storageService.loadAsResource(anyString())).thenReturn(resource);

        List<Resource> images = imageService.getAllImagesByEstateId(1L);

        assertThat(images, hasSize(1));
        verify(imageRepository, times(1)).findAllByEstateId(anyLong());
        verify(storageService, times(1)).loadAsResource(anyString());
        verify(image, times(1)).getName();
    }

    @Test
    public void verifyGetAllImagesByEstateIdWhenEstateIdIsNotPresent() {
        when(imageRepository.findAllByEstateId(anyLong())).thenReturn(Collections.emptyList());

        List<Resource> images = imageService.getAllImagesByEstateId(1L);

        assertThat(images, is(empty()));
        verify(imageRepository, times(1)).findAllByEstateId(anyLong());
        verify(storageService, never()).loadAsResource(anyString());
    }

    @Test
    public void verifyCreateImage() {
        final Estate estate = mock(Estate.class);
        final MultipartFile file = mock(MultipartFile.class);

        when(estateRepository.findById(anyLong())).thenReturn(Optional.of(estate));
        when(storageService.store(any(MultipartFile.class))).thenReturn(StringUtils.EMPTY);
        when(imageRepository.save(any(Image.class))).thenReturn(image);

        imageService.createImage(file, 1L);

        verify(estateRepository, times(1)).findById(anyLong());
        verify(storageService, times(1)).store(any(MultipartFile.class));
        verify(imageRepository, times(1)).save(any(Image.class));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void verifyCreateImageThrowsWhenEstateIdIsNotPresent() {
        final MultipartFile file = mock(MultipartFile.class);

        when(estateRepository.findById(anyLong())).thenReturn(Optional.empty());

        imageService.createImage(file, 1L);

        verify(estateRepository, times(1)).findById(anyLong());
        verify(storageService, never()).store(any(MultipartFile.class));
        verify(imageRepository, never()).save(any(Image.class));
    }

    @Test
    public void verifyDeleteImage() {
        when(imageRepository.findByName(anyString())).thenReturn(Optional.of(image));
        doNothing().when(imageRepository).delete(any(Image.class));
        doNothing().when(storageService).delete(anyString());

        imageService.deleteImage(StringUtils.EMPTY);

        verify(imageRepository, times(1)).findByName(anyString());
        verify(imageRepository, times(1)).delete(any(Image.class));
        verify(storageService, times(1)).delete(anyString());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void verifyDeleteImageThrowsWhenImageIsNotFound() {
        when(imageRepository.findByName(anyString())).thenReturn(Optional.empty());

        imageService.deleteImage(StringUtils.EMPTY);

        verify(imageRepository, times(1)).findByName(anyString());
        verify(imageRepository, never()).delete(any(Image.class));
        verify(storageService, never()).delete(anyString());
    }
}
