package com.memento.web.endpoint.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(value = "Image API")
public interface ImageApi {

    @ApiOperation(value = "Find one image", response = Resource.class)
    ResponseEntity<Resource> findOneImage(String imageName);

    @ApiOperation(value = "Get all images by estate id", response = List.class)
    ResponseEntity<List<Resource>> getAllImagesByEstateId(Long estateId);

    @ApiOperation(value = "Create image")
    ResponseEntity<HttpStatus> createImage(MultipartFile file, Long estateId);

    @ApiOperation(value = "Delete image")
    ResponseEntity<HttpStatus> deleteImage(String imageName);
}
