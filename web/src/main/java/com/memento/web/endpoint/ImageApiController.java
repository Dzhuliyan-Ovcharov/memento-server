package com.memento.web.endpoint;

import com.memento.service.ImageService;
import com.memento.web.endpoint.api.ImageApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    @GetMapping(value = "/name/{imageName}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<Resource> findOneImage(@PathVariable(value = "imageName") final String imageName) {
        return ResponseEntity.ok(imageService.findOneImage(imageName));
    }

    @Override
    @GetMapping(value = "/estate/{estateId}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<List<Resource>> getAllImagesByEstateId(@PathVariable(value = "estateId") final Long estateId) {
        return ResponseEntity.ok(imageService.getAllImagesByEstateId(estateId));
    }

    @Override
    @PostMapping(value = "/estate/{estateId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HttpStatus> createImage(@RequestParam(value = "file") final MultipartFile file, @PathVariable(value = "estateId") final Long estateId) {
        imageService.createImage(file, estateId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/all/estate/{estateId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HttpStatus> createImages(@RequestParam(value = "files") final List<MultipartFile> files, @PathVariable(value = "estateId") final Long estateId) {
        imageService.createImages(files, estateId);
        return ResponseEntity.ok().build();
    }

    @Override
    @DeleteMapping(value = "/name/{imageName}")
    public ResponseEntity<HttpStatus> deleteImage(@PathVariable(value = "imageName") final String imageName) {
        imageService.deleteImage(imageName);
        return ResponseEntity.ok().build();
    }
}
