package com.memento.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ImageService {

    Resource findOneImage(String imageName);

    List<Resource> getAllImagesByEstateId(Long estateId);

    void createImage(MultipartFile file, Long estateId);

    void createImages(List<MultipartFile> files, Long estateId);

    void deleteImage(String imageName);
}
