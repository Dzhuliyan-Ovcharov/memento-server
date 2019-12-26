package com.memento.service.impl;

import com.memento.service.StorageService;
import com.memento.shared.exception.MementoException;
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

    private final String storageRootDirectory;

    @Autowired
    public ImageStorageService(final ResourceLoader resourceLoader,
                               @Value("${memento.upload-dir}") final String storageRootDirectory) {
        this.resourceLoader = resourceLoader;
        this.storageRootDirectory = storageRootDirectory;
    }

    @Override
    public String store(final MultipartFile file) {
        final String newFileName = generateFileName(file);
        try {
            if (file.isEmpty() || ImageIO.read(file.getInputStream()) == null ||
                    !FilenameUtils.isExtension(file.getOriginalFilename(), SUPPORTED_FILE_EXTENSIONS)) {
                log.error("The file {} is either empty or its type is not supported.", file.getOriginalFilename());
                throw new StorageException(String.format("The file %s is either empty or its not an image with supported extension. Current supported file extensions are %s.", file.getOriginalFilename(), SUPPORTED_FILE_EXTENSIONS));
            }
            file.transferTo(Path.of(storageRootDirectory, newFileName));
        } catch (IOException e) {
            log.error("Cannot create file {}.{}", newFileName, e.getMessage());
            throw new MementoException("Cannot store file " + file.getOriginalFilename(), e);
        }
        return newFileName;
    }

    @Override
    public void delete(final String fileName) {
        try {
            Files.delete(Path.of(storageRootDirectory, fileName));
        } catch (IOException e) {
            log.error("Cannot delete file {}.{}", fileName, e.getMessage());
            throw new MementoException("Cannot delete file " + fileName, e);
        }
    }

    @Override
    public Resource loadAsResource(final String fileName) {
        return resourceLoader.getResource(ResourceUtils.FILE_URL_PREFIX + Path.of(storageRootDirectory, fileName).toString());
    }

    private String generateFileName(final MultipartFile file) {
        final String uid = UUID.randomUUID().toString();
        final String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());

        return uid + "." + fileExtension;
    }
}
