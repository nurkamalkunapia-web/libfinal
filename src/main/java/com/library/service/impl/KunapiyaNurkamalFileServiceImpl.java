package com.library.service.impl;

import com.library.exception.KunapiyaNurkamalResourceNotFoundException;
import com.library.service.interfaces.KunapiyaNurkamalFileService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
public class KunapiyaNurkamalFileServiceImpl implements KunapiyaNurkamalFileService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private Path uploadPath;

    @PostConstruct
    public void init() throws IOException {
        uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            log.info("Created upload directory: {}", uploadDir);
        }
    }

    @Override
    public String uploadFile(MultipartFile file) {
        log.info("Uploading file: {}", file.getOriginalFilename());

        try {
            String originalName = file.getOriginalFilename();
            String extension = "";
            if (originalName != null && originalName.contains(".")) {
                extension = originalName.substring(originalName.lastIndexOf("."));
            }

            String newFileName = UUID.randomUUID().toString() + extension;
            Path filePath = uploadPath.resolve(newFileName);
            Files.copy(file.getInputStream(), filePath);

            log.info("File uploaded successfully: {}", newFileName);
            return newFileName;

        } catch (IOException e) {
            log.error("File upload failed: {}", e.getMessage());
            throw new RuntimeException("File upload failed", e);
        }
    }

    @Override
    public Resource downloadFile(String filename) {
        log.info("Downloading file: {}", filename);

        try {
            Path filePath = uploadPath.resolve(filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new KunapiyaNurkamalResourceNotFoundException("File not found: " + filename);
            }
        } catch (IOException e) {
            log.error("File download failed: {}", e.getMessage());
            throw new RuntimeException("File download failed", e);
        }
    }
}