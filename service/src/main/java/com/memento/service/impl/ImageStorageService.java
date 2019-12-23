package com.memento.service.impl;

import com.memento.service.StorageService;
import com.memento.shared.exception.StorageException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ImageStorageService implements StorageService {

    private static final List<String> SUPPORTED_FILE_EXTENSIONS = List.of("jpg", "png");

    private final ResourceLoader resourceLoader;

    @Value(value = "${memento.upload-dir}")
    private final String storageRootDirectory;

    @Autowired
    public ImageStorageService(final ResourceLoader resourceLoader, final String storageRootDirectory) {
        this.resourceLoader = resourceLoader;
        this.storageRootDirectory = storageRootDirectory;
    }

    @Override
    public String store(final MultipartFile file) {
        if (file.isEmpty()) {
            log.error("The file {} is empty.", file.getOriginalFilename());
            throw new StorageException("Cannot store empty file " + file.getOriginalFilename());
        }

        final String newFileName = generateFileName(file);
        final Path filePath = Path.of(storageRootDirectory, newFileName);

        try {
            if (ImageIO.read(file.getInputStream()) == null) {
                log.error("The file {} is not an image.", file.getOriginalFilename());
                throw new StorageException("Cannot store non image file " + file.getOriginalFilename());
            }
            file.transferTo(filePath);
        } catch (IOException e) {
            log.error("Cannot create file {}.{}", newFileName, e.getMessage());
            throw new StorageException("Cannot store file " + file.getOriginalFilename(), e);
        }
        return newFileName;
    }

    @Override
    public void delete(final String fileName) {
        if (fileName.contains("..")) {
            log.error("Cannot delete file {} with relative path outside current directory.", fileName);
            throw new StorageException("Cannot delete file with relative path outside current directory " + fileName);
        }
        try {
            Files.delete(Path.of(storageRootDirectory, fileName));
        } catch (IOException e) {
            log.error("Cannot delete file {}.{}", fileName, e.getMessage());
            throw new StorageException("Cannot delete file " + fileName, e);
        }
    }

    @Override
    public Resource loadAsResource(final String fileName) {
        return resourceLoader.getResource(ResourceUtils.FILE_URL_PREFIX + Path.of(storageRootDirectory, fileName).toString());
    }

    private String generateFileName(final MultipartFile file) {
        final String uid = UUID.randomUUID().toString();
        final String fileExtension = getFileExtension(file);

        return uid + "." + fileExtension;
    }

    private String getFileExtension(final MultipartFile file) {
        if (!FilenameUtils.isExtension(file.getOriginalFilename(), SUPPORTED_FILE_EXTENSIONS)) {
            log.error("File type of {} is not supported", file.getOriginalFilename());
            throw new StorageException("File type not supported " + file.getOriginalFilename());
        }

        return FilenameUtils.getExtension(file.getOriginalFilename());
    }
}
