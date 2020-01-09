package com.memento.web.endpoint;

import com.memento.model.Permission;
import com.memento.service.ImageService;
import com.memento.web.endpoint.api.ImageApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.memento.web.RequestUrlConstant.IMAGES_BASE_URL;

@RestController
@RequestMapping(value = IMAGES_BASE_URL, produces = {MediaType.APPLICATION_JSON_VALUE})
public class ImageApiController implements ImageApi {

    private final ImageService imageService;

    @Autowired
    public ImageApiController(final ImageService imageService) {
        this.imageService = imageService;
    }

    @Override
    @GetMapping(params = "name", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<Resource> findOneImage(@RequestParam(value = "name") final String imageName) {
        return ResponseEntity.ok(imageService.findOneImage(imageName));
    }

    @Override
    @GetMapping(params = "estate_id", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<List<Resource>> getAllImagesByEstateId(@RequestParam(value = "estate_id") final Long estateId) {
        return ResponseEntity.ok(imageService.getAllImagesByEstateId(estateId));
    }

    @Override
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HttpStatus> createImage(@RequestParam(value = "file") final MultipartFile file, @RequestParam(value = "estate_id") final Long estateId) {
        imageService.createImage(file, estateId);
        return ResponseEntity.ok().build();
    }

    @Override
    @Secured(Permission.Value.ADMIN)
    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteImage(@RequestParam(value = "name") final String imageName) {
        imageService.deleteImage(imageName);
        return ResponseEntity.ok().build();
    }
}
