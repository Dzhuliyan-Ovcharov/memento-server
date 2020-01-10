package com.memento.web.endpoint;

import com.memento.model.Permission;
import com.memento.service.ImageService;
import com.memento.shared.exception.MementoException;
import com.memento.shared.exception.ResourceNotFoundException;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.memento.web.RequestUrlConstant.IMAGES_BASE_URL;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ImageApiController.class)
public class ImageApiControllerTest extends BaseApiControllerTest {

    private static final Long ESTATE_ID = 1L;
    private static final String IMAGE_NAME = "image.jpg";

    private MockMultipartFile file;

    @MockBean
    private ImageService imageService;

    @Before
    public void init() {
        file = new MockMultipartFile(
                "file",
                IMAGE_NAME,
                MediaType.IMAGE_JPEG_VALUE,
                IMAGE_NAME.getBytes());
    }

    @Test
    public void verifyFindOneImageAndExpect200() throws Exception {
        final InputStream inputStream = IOUtils.toInputStream(IMAGE_NAME, StandardCharsets.UTF_8);
        final Resource resource = mock(Resource.class);

        when(imageService.findOneImage(IMAGE_NAME)).thenReturn(resource);
        when(resource.getInputStream()).thenReturn(inputStream);

        mockMvc.perform(
                get(IMAGES_BASE_URL + "/name/" + IMAGE_NAME)
                        .accept(MediaType.IMAGE_JPEG, MediaType.IMAGE_PNG))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG));

        verify(imageService, times(1)).findOneImage(IMAGE_NAME);
    }

    @Test
    public void verifyFindOneImageWhenImageIsNotFoundAndExpect404() throws Exception {
        when(imageService.findOneImage(IMAGE_NAME)).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(
                get(IMAGES_BASE_URL + "/name/" + IMAGE_NAME)
                        .accept(MediaType.IMAGE_JPEG, MediaType.IMAGE_PNG))
                .andExpect(status().isNotFound());

        verify(imageService, times(1)).findOneImage(IMAGE_NAME);
    }

    @Test
    public void verifyCreateImageAndExpect200() throws Exception {
        doNothing().when(imageService).createImage(file, ESTATE_ID);

        mockMvc.perform(
                multipart(IMAGES_BASE_URL + "/estate/" + ESTATE_ID)
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        verify(imageService, times(1)).createImage(file, ESTATE_ID);
    }

    @Test
    public void verifyCreateImageWhenEstateIdIsNotFoundAndExpect404() throws Exception {
        doThrow(ResourceNotFoundException.class).when(imageService).createImage(file, ESTATE_ID);

        mockMvc.perform(
                multipart(IMAGES_BASE_URL + "/estate/" + ESTATE_ID)
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isNotFound());

        verify(imageService, times(1)).createImage(file, ESTATE_ID);
    }

    @Test
    public void verifyCreateImageWhenIOErrorOccursAndExpect500() throws Exception {
        doThrow(MementoException.class).when(imageService).createImage(file, ESTATE_ID);

        mockMvc.perform(
                multipart(IMAGES_BASE_URL + "/estate/" + ESTATE_ID)
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isInternalServerError());

        verify(imageService, times(1)).createImage(file, ESTATE_ID);
    }

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifyDeleteImageAndExpect200() throws Exception {
        doNothing().when(imageService).deleteImage(IMAGE_NAME);

        mockMvc.perform(
                delete(IMAGES_BASE_URL + "/name/" + IMAGE_NAME))
                .andExpect(status().isOk());

        verify(imageService, times(1)).deleteImage(IMAGE_NAME);
    }

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifyDeleteImageWhenImageIsNotFoundAndExpect404() throws Exception {
        doThrow(ResourceNotFoundException.class).when(imageService).deleteImage(IMAGE_NAME);

        mockMvc.perform(
                delete(IMAGES_BASE_URL + "/name/" + IMAGE_NAME))
                .andExpect(status().isNotFound());

        verify(imageService, times(1)).deleteImage(IMAGE_NAME);
    }

    @Test
    @WithMockUser(authorities = Permission.Value.ADMIN)
    public void verifyDeleteImageWhenIOErrorOccursAndExpect500() throws Exception {
        doThrow(MementoException.class).when(imageService).deleteImage(IMAGE_NAME);

        mockMvc.perform(
                delete(IMAGES_BASE_URL + "/name/" + IMAGE_NAME))
                .andExpect(status().isInternalServerError());

        verify(imageService, times(1)).deleteImage(IMAGE_NAME);
    }

    @Test
    @WithMockUser(authorities = {Permission.Value.AGENCY, Permission.Value.BUYER})
    public void verifyDeleteImageWhenUserIsNotAuthorizedAndExpect403() throws Exception {
        mockMvc.perform(
                delete(IMAGES_BASE_URL + "/name/" + IMAGE_NAME))
                .andExpect(status().isForbidden());

        verify(imageService, never()).deleteImage(anyString());
    }

    @Test
    public void verifyDeleteImageWhenUserIsNotAuthenticatedAndExpect401() throws Exception {
        mockMvc.perform(
                delete(IMAGES_BASE_URL + "/name/" + IMAGE_NAME))
                .andExpect(status().isUnauthorized());

        verify(imageService, never()).deleteImage(anyString());
    }
}
