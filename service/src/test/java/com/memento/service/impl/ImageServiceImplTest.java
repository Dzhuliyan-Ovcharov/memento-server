package com.memento.service.impl;

import com.memento.model.Estate;
import com.memento.model.Image;
import com.memento.repository.EstateRepository;
import com.memento.repository.ImageRepository;
import com.memento.service.StorageService;
import com.memento.shared.exception.ResourceNotFoundException;
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

    private static final Long ESTATE_ID = 1L;
    private static final String IMAGE_NAME = "image.jpg";

    private Image image;

    @Mock
    private StorageService storageService;

    @Mock
    private EstateRepository estateRepository;

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageServiceImpl imageService;

    @Before
    public void setUp() {
        image = mock(Image.class);
    }

    @Test
    public void verifyFindOneImageWhenImageIsFound() {
        final Resource resource = mock(Resource.class);
        when(imageRepository.findByName(IMAGE_NAME)).thenReturn(Optional.of(image));
        when(image.getName()).thenReturn(IMAGE_NAME);
        when(storageService.loadAsResource(IMAGE_NAME)).thenReturn(resource);

        imageService.findOneImage(IMAGE_NAME);

        verify(imageRepository, times(1)).findByName(IMAGE_NAME);
        verify(image, times(1)).getName();
        verify(storageService, times(1)).loadAsResource(IMAGE_NAME);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void verifyFindOneImageThrowsWhenImageIsNotFound() {
        when(imageRepository.findByName(IMAGE_NAME)).thenReturn(Optional.empty());

        imageService.findOneImage(IMAGE_NAME);

        verify(imageRepository, times(1)).findByName(IMAGE_NAME);
        verify(storageService, never()).loadAsResource(anyString());
    }

    @Test(expected = NullPointerException.class)
    public void verifyFindOneImageThrowsWhenImageNameIsNull() {
        imageService.findOneImage(null);
    }

    @Test
    public void verifyGetAllImagesByEstateId() {
        final Resource resource = mock(Resource.class);
        when(imageRepository.findAllByEstateId(ESTATE_ID)).thenReturn(Collections.singletonList(image));
        when(image.getName()).thenReturn(IMAGE_NAME);
        when(storageService.loadAsResource(IMAGE_NAME)).thenReturn(resource);

        final List<Resource> images = imageService.getAllImagesByEstateId(ESTATE_ID);

        assertThat(images, hasSize(1));
        verify(imageRepository, times(1)).findAllByEstateId(ESTATE_ID);
        verify(storageService, times(1)).loadAsResource(IMAGE_NAME);
        verify(image, times(1)).getName();
    }

    @Test
    public void verifyGetAllImagesByEstateIdWhenEstateIdIsNotPresent() {
        when(imageRepository.findAllByEstateId(ESTATE_ID)).thenReturn(Collections.emptyList());

        final List<Resource> images = imageService.getAllImagesByEstateId(ESTATE_ID);

        assertThat(images, is(empty()));
        verify(imageRepository, times(1)).findAllByEstateId(ESTATE_ID);
        verify(storageService, never()).loadAsResource(anyString());
    }

    @Test(expected = NullPointerException.class)
    public void verifyGetAllImagesByEstateIdThrowsWhenEstateIdIsNull() {
        imageService.getAllImagesByEstateId(null);
    }

    @Test
    public void verifyCreateImage() {
        final Estate estate = mock(Estate.class);
        final MultipartFile file = mock(MultipartFile.class);

        when(estateRepository.findById(ESTATE_ID)).thenReturn(Optional.of(estate));
        when(storageService.store(file)).thenReturn(IMAGE_NAME);
        when(imageRepository.save(any(Image.class))).thenReturn(image);

        imageService.createImage(file, ESTATE_ID);

        verify(estateRepository, times(1)).findById(ESTATE_ID);
        verify(storageService, times(1)).store(file);
        verify(imageRepository, times(1)).save(any(Image.class));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void verifyCreateImageThrowsWhenEstateIdIsNotPresent() {
        final MultipartFile file = mock(MultipartFile.class);

        when(estateRepository.findById(ESTATE_ID)).thenReturn(Optional.empty());

        imageService.createImage(file, ESTATE_ID);

        verify(estateRepository, times(1)).findById(ESTATE_ID);
        verify(storageService, never()).store(any(MultipartFile.class));
        verify(imageRepository, never()).save(any(Image.class));
    }

    @Test(expected = NullPointerException.class)
    public void verifyCreateImageThrowsWhenFileIsNull() {
        imageService.createImage(null, ESTATE_ID);
    }

    @Test(expected = NullPointerException.class)
    public void verifyCreateImageThrowsWhenEstateIdIsNull() {
        final MultipartFile file = mock(MultipartFile.class);
        imageService.createImage(file, null);
    }

    @Test
    public void verifyDeleteImage() {
        when(imageRepository.findByName(IMAGE_NAME)).thenReturn(Optional.of(image));
        doNothing().when(imageRepository).delete(image);
        doNothing().when(storageService).delete(IMAGE_NAME);

        imageService.deleteImage(IMAGE_NAME);

        verify(imageRepository, times(1)).findByName(IMAGE_NAME);
        verify(imageRepository, times(1)).delete(image);
        verify(storageService, times(1)).delete(IMAGE_NAME);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void verifyDeleteImageThrowsWhenImageIsNotFound() {
        when(imageRepository.findByName(IMAGE_NAME)).thenReturn(Optional.empty());

        imageService.deleteImage(IMAGE_NAME);

        verify(imageRepository, times(1)).findByName(IMAGE_NAME);
        verify(imageRepository, never()).delete(any(Image.class));
        verify(storageService, never()).delete(anyString());
    }

    @Test(expected = NullPointerException.class)
    public void verifyDeleteImageThrowsWhenImageNameIsNull() {
        imageService.deleteImage(null);
    }
}
