package com.memento.service.impl;

import com.memento.shared.exception.MementoException;
import com.memento.shared.exception.StorageException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(value = {
        ImageIO.class,
        FileUtils.class,
        FilenameUtils.class,
        StringUtils.class})
public class ImageStorageServiceTest {

    @Mock
    private ResourceLoader resourceLoader;

    @Mock
    private String storageRootDirectory;

    @InjectMocks
    private ImageStorageService imageStorageService;

    @Before
    public void setUp() {
        mockStatic(FileUtils.class);
        mockStatic(FilenameUtils.class);
        mockStatic(ImageIO.class);
        mockStatic(StringUtils.class);
    }

    @Test
    public void verifyStore() throws IOException {
        final MultipartFile file = mock(MultipartFile.class);
        final InputStream inputStream = mock(InputStream.class);
        final BufferedImage image = mock(BufferedImage.class);
        final File destination = mock(File.class);

        when(file.getOriginalFilename()).thenReturn("image.jpg");
        when(FilenameUtils.getExtension(anyString())).thenReturn("jpg");
        when(file.isEmpty()).thenReturn(false);
        when(file.getInputStream()).thenReturn(inputStream);
        when(ImageIO.read(any(InputStream.class))).thenReturn(image);
        when(FilenameUtils.isExtension(anyString(), anyCollection())).thenReturn(true);
        when(FileUtils.getFile(anyString(), anyString())).thenReturn(destination);
        doNothing().when(file).transferTo(any(File.class));

        final String result = imageStorageService.store(file);

        assertThat(result, not(isEmptyOrNullString()));

        verify(file, times(2)).getOriginalFilename();
        verifyStatic(FilenameUtils.class, times(1));
        FilenameUtils.getExtension(anyString());
        verify(file, times(1)).isEmpty();
        verify(file, times(1)).getInputStream();
        verifyStatic(ImageIO.class, times(1));
        ImageIO.read(any(InputStream.class));
        verifyStatic(FilenameUtils.class, times(1));
        FilenameUtils.isExtension(anyString(), anyCollection());
        verifyStatic(FileUtils.class, times(1));
        FileUtils.getFile(anyString(), anyString());
        verify(file, times(1)).transferTo(any(File.class));
    }

    @Test(expected = NullPointerException.class)
    public void verifyStoreThrowsWhenFileIsNull() {
        imageStorageService.store(null);
    }

    @Test(expected = StorageException.class)
    public void verifyStoreThrowsWhenFileIsEmpty() {
        final MultipartFile file = mock(MultipartFile.class);

        when(file.getOriginalFilename()).thenReturn("image.jpg");
        when(FilenameUtils.getExtension(anyString())).thenReturn("jpg");
        when(file.isEmpty()).thenReturn(true);

        imageStorageService.store(file);
    }

    @Test(expected = StorageException.class)
    public void verifyStoreThrowsWhenFileIsNotImage() throws IOException {
        final MultipartFile file = mock(MultipartFile.class);
        final InputStream inputStream = mock(InputStream.class);

        when(file.getOriginalFilename()).thenReturn("image.jpg");
        when(FilenameUtils.getExtension(anyString())).thenReturn("jpg");
        when(file.isEmpty()).thenReturn(false);
        when(file.getInputStream()).thenReturn(inputStream);
        when(ImageIO.read(any(InputStream.class))).thenReturn(null);

        imageStorageService.store(file);
    }

    @Test(expected = StorageException.class)
    public void verifyStoreThrowsWhenFileExtensionIsNotSupported() throws IOException {
        final MultipartFile file = mock(MultipartFile.class);
        final InputStream inputStream = mock(InputStream.class);
        final BufferedImage image = mock(BufferedImage.class);

        when(file.getOriginalFilename()).thenReturn("image.jpg");
        when(FilenameUtils.getExtension(anyString())).thenReturn("jpg");
        when(file.isEmpty()).thenReturn(false);
        when(file.getInputStream()).thenReturn(inputStream);
        when(ImageIO.read(any(InputStream.class))).thenReturn(image);
        when(FilenameUtils.isExtension(anyString(), anyCollection())).thenReturn(false);

        imageStorageService.store(file);
    }

    @Test
    public void verifyDelete() throws IOException {
        final File file = mock(File.class);

        when(StringUtils.isBlank(anyString())).thenReturn(false);
        when(FileUtils.getFile(anyString(), anyString())).thenReturn(file);
        when(file.isDirectory()).thenReturn(false);
        PowerMockito.doNothing().when(FileUtils.class);
        FileUtils.forceDelete(any(File.class));

        imageStorageService.delete("");

        verifyStatic(StringUtils.class, times(1));
        StringUtils.isBlank(anyString());
        verifyStatic(FileUtils.class, times(1));
        FileUtils.getFile(anyString(), anyString());
        verify(file, times(1)).isDirectory();
        verifyStatic(FileUtils.class, times(1));
        FileUtils.forceDelete(any(File.class));
    }

    @Test(expected = MementoException.class)
    public void verifyDeleteThrowsWhenFileCannotBeDeleted() throws IOException {
        final File file = mock(File.class);

        when(StringUtils.isBlank(anyString())).thenReturn(false);
        when(FileUtils.getFile(anyString(), anyString())).thenReturn(file);
        when(file.isDirectory()).thenReturn(false);
        PowerMockito.doThrow(new IOException()).when(FileUtils.class);
        FileUtils.forceDelete(any(File.class));

        imageStorageService.delete("");
    }

    @Test(expected = StorageException.class)
    public void verifyDeleteThrowsWhenFileNameIsBlank() {
        when(StringUtils.isBlank(anyString())).thenReturn(true);

        imageStorageService.delete("");
    }

    @Test(expected = StorageException.class)
    public void verifyDeleteThrowsWhenFileNameIsDirectory() {
        final File file = mock(File.class);
        when(StringUtils.isBlank(anyString())).thenReturn(false);
        when(FileUtils.getFile(anyString(), anyString())).thenReturn(file);
        when(file.isDirectory()).thenReturn(true);

        imageStorageService.delete("");
    }

    @Test
    public void verifyLoadAsResource() {
        final Resource resource = mock(Resource.class);
        final File file = mock(File.class);

        when(StringUtils.isBlank(anyString())).thenReturn(false);
        when(FileUtils.getFile(anyString(), anyString())).thenReturn(file);
        when(file.isDirectory()).thenReturn(false);
        when(file.toString()).thenReturn("");
        when(resourceLoader.getResource(anyString())).thenReturn(resource);

        imageStorageService.loadAsResource("");

        verifyStatic(StringUtils.class, times(1));
        StringUtils.isBlank(anyString());
        verifyStatic(FileUtils.class, times(1));
        FileUtils.getFile(anyString(), anyString());
        verify(file, times(1)).isDirectory();
        verify(resourceLoader, times(1)).getResource(anyString());
    }

    @Test(expected = StorageException.class)
    public void verifyLoadAsResourceThrowsWhenFileNameIsBlank() {
        when(StringUtils.isBlank(anyString())).thenReturn(true);

        imageStorageService.delete("");
    }

    @Test(expected = StorageException.class)
    public void verifyLoadAsResourceThrowsWhenFileNameIsDirectory() {
        final File file = mock(File.class);
        when(StringUtils.isBlank(anyString())).thenReturn(false);
        when(FileUtils.getFile(anyString(), anyString())).thenReturn(file);
        when(file.isDirectory()).thenReturn(true);

        imageStorageService.delete("");
    }
}
