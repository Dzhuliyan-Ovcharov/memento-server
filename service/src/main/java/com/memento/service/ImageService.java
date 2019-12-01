package com.memento.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {

    Resource findOneImage(String path);

    List<Resource> getAllImagesByEstateId(Long estateId);

    void createImage(MultipartFile file);

    void deleteImage(String path);
}
