package com.memento.web.endpoint;

import com.memento.model.Permission;
import com.memento.service.ImageService;
import com.memento.shared.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ImageApiController.class)
public class ImageApiControllerTest extends BaseApiControllerTest {

    @MockBean
    private ImageService imageService;

    private MockMultipartFile file;

    @Before
    public void init() {
        file = new MockMultipartFile(
                "file",
                "image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "image".getBytes());
    }

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifyCreateImageAndExpect200() throws Exception {
        doNothing().when(imageService).createImage(any(MultipartFile.class), anyLong());

        mockMvc.perform(
                multipart("/api/image/create/estate/id/1")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        verify(imageService, times(1)).createImage(any(MultipartFile.class), anyLong());
    }

    @Test
    @WithMockUser(authorities = {Permission.Value.AGENCY, Permission.Value.BUYER})
    public void verifyCreateImageWhenUserIsNotAuthorizedAndExpect403() throws Exception {
        mockMvc.perform(
                multipart("/api/image/create/estate/id/1")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isForbidden());

        verify(imageService, never()).createImage(any(MultipartFile.class), anyLong());
    }

    @Test
    public void verifyCreateImageWhenUserIsNotAuthenticatedAndExpect401() throws Exception {
        mockMvc.perform(
                multipart("/api/image/create/estate/id/1")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isUnauthorized());

        verify(imageService, never()).createImage(any(MultipartFile.class), anyLong());
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
