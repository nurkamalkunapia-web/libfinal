package com.library.service.interfaces;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface KunapiyaNurkamalFileService {

    String uploadFile(MultipartFile file);

    Resource downloadFile(String filename);
}