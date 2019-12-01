package com.memento.service.impl;

import com.memento.model.Image;
import com.memento.repository.ImageRepository;
import com.memento.service.ImageService;
import com.memento.shared.exception.ResourceNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {

    private static final String UPLOAD_ROOT = "upload-dir";

    private final ImageRepository imageRepository;
    private final ResourceLoader resourceLoader;

    public ImageServiceImpl(final ImageRepository imageRepository, final ResourceLoader resourceLoader) {
        this.imageRepository = imageRepository;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public Resource findOneImage(String path) {
        final Image image = imageRepository.findByPath(path).orElseThrow(ResourceNotFoundException::new);
        return resourceLoader.getResource("file:" + UPLOAD_ROOT + "/" + image.getPath());
    }

    @Override
    public List<Resource> getAllImagesByEstateId(Long estateId) {
        return null;
    }

    @Override
    public void createImage(MultipartFile file) {

    }

    @Override
    public void deleteImage(String path) {

    }
}
