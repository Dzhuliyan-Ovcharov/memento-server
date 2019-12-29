package com.memento.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    String store(MultipartFile file);

    void delete(String fileName);

    Resource loadAsResource(String fileName);
}
