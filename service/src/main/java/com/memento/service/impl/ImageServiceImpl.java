package com.memento.service.impl;

import com.memento.model.Estate;
import com.memento.model.Image;
import com.memento.repository.EstateRepository;
import com.memento.repository.ImageRepository;
import com.memento.service.ImageService;
import com.memento.service.StorageService;
import com.memento.shared.exception.ResourceNotFoundException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageServiceImpl implements ImageService {

    private final StorageService storageService;
    private final EstateRepository estateRepository;
    private final ImageRepository imageRepository;

    @Autowired
    public ImageServiceImpl(final StorageService storageService,
                            final EstateRepository estateRepository,
                            final ImageRepository imageRepository) {
        this.storageService = storageService;
        this.estateRepository = estateRepository;
        this.imageRepository = imageRepository;
    }

    @Override
    public Resource findOneImage(@NonNull final String imageName) {
        final Image image = imageRepository.findByName(imageName).orElseThrow(ResourceNotFoundException::new);
        return storageService.loadAsResource(image.getName());
    }

    @Override
    public List<Resource> getAllImagesByEstateId(@NonNull final Long estateId) {
        return imageRepository.findAllByEstateId(estateId)
                .stream()
                .map(image -> storageService.loadAsResource(image.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public void createImage(@NonNull final MultipartFile file, @NonNull final Long estateId) {
        final Estate estate = estateRepository.findById(estateId).orElseThrow(ResourceNotFoundException::new);
        final String imageName = storageService.store(file);
        imageRepository.save(Image.builder()
                .name(imageName)
                .estate(estate)
                .build());
    }

    @Override
    public void deleteImage(@NonNull final String imageName) {
        final Image image = imageRepository.findByName(imageName).orElseThrow(ResourceNotFoundException::new);
        imageRepository.delete(image);
        storageService.delete(imageName);
    }
}
