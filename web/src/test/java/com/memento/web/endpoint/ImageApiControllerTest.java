package com.memento.web.endpoint;

import com.memento.model.Permission;
import com.memento.service.ImageService;
import com.memento.shared.exception.MementoException;
import com.memento.shared.exception.ResourceNotFoundException;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ImageApiController.class)
public class ImageApiControllerTest extends BaseApiControllerTest {

    @MockBean
    private ImageService imageService;

    @Test
    public void verifyFindOneImageAndExpect200() throws Exception {
        final InputStream inputStream = IOUtils.toInputStream("image", StandardCharsets.UTF_8);
        final Resource resource = mock(Resource.class);

        when(imageService.findOneImage(anyString())).thenReturn(resource);
        when(resource.getInputStream()).thenReturn(inputStream);

        mockMvc.perform(
                get("/api/image/name/image_name")
                        .accept(MediaType.IMAGE_JPEG, MediaType.IMAGE_PNG))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG));

        verify(imageService, times(1)).findOneImage(anyString());
    }

    @Test
    public void verifyFindOneImageWhenImageIsNotFoundAndExpect404() throws Exception {
        when(imageService.findOneImage(anyString())).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(
                get("/api/image/name/image_name")
                        .accept(MediaType.IMAGE_JPEG, MediaType.IMAGE_PNG))
                .andExpect(status().isNotFound());

        verify(imageService, times(1)).findOneImage(anyString());
    }

    @Test
    public void verifyCreateImageAndExpect200() throws Exception {
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "image".getBytes());

        doNothing().when(imageService).createImage(any(MultipartFile.class), anyLong());

        mockMvc.perform(
                multipart("/api/image/create/estate/id/1")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        verify(imageService, times(1)).createImage(any(MultipartFile.class), anyLong());
    }

    @Test
    public void verifyCreateImageWhenEstateIdIsNotFoundAndExpect404() throws Exception {
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "image".getBytes());

        doThrow(ResourceNotFoundException.class).when(imageService).createImage(any(MultipartFile.class), anyLong());

        mockMvc.perform(
                multipart("/api/image/create/estate/id/1")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isNotFound());

        verify(imageService, times(1)).createImage(any(MultipartFile.class), anyLong());
    }

    @Test
    public void verifyCreateImageWhenIOErrorOccursAndExpect500() throws Exception {
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "image".getBytes());

        doThrow(MementoException.class).when(imageService).createImage(any(MultipartFile.class), anyLong());

        mockMvc.perform(
                multipart("/api/image/create/estate/id/1")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isInternalServerError());

        verify(imageService, times(1)).createImage(any(MultipartFile.class), anyLong());
    }

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifyDeleteImageAndExpect200() throws Exception {
        doNothing().when(imageService).deleteImage(anyString());

        mockMvc.perform(
                delete("/api/image/delete/name/image_name"))
                .andExpect(status().isOk());

        verify(imageService, times(1)).deleteImage(anyString());
    }

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifyDeleteImageWhenImageIsNotFoundAndExpect404() throws Exception {
        doThrow(ResourceNotFoundException.class).when(imageService).deleteImage(anyString());

        mockMvc.perform(
                delete("/api/image/delete/name/image_name"))
                .andExpect(status().isNotFound());

        verify(imageService, times(1)).deleteImage(anyString());
    }

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifyDeleteImageWhenIOErrorOccursAndExpect500() throws Exception {
        doThrow(MementoException.class).when(imageService).deleteImage(anyString());

        mockMvc.perform(
                delete("/api/image/delete/name/image_name"))
                .andExpect(status().isInternalServerError());

        verify(imageService, times(1)).deleteImage(anyString());
    }

    @Test
    @WithMockUser(authorities = {Permission.Value.AGENCY, Permission.Value.BUYER})
    public void verifyDeleteImageWhenUserIsNotAuthorizedAndExpect403() throws Exception {
        mockMvc.perform(
                delete("/api/image/delete/name/image_name"))
                .andExpect(status().isForbidden());

        verify(imageService, never()).deleteImage(anyString());
    }

    @Test
    public void verifyDeleteImageWhenUserIsNotAuthenticatedAndExpect401() throws Exception {
        mockMvc.perform(
                delete("/api/image/delete/name/image_name"))
                .andExpect(status().isUnauthorized());

        verify(imageService, never()).deleteImage(anyString());
    }
}
