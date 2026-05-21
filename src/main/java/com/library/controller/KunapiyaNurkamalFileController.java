package com.library.controller;

import com.library.service.interfaces.KunapiyaNurkamalFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Tag(name = "Files", description = "File upload and download endpoints")
public class KunapiyaNurkamalFileController {

    private final KunapiyaNurkamalFileService fileService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload a file", description = "Uploads a file to the server")
    public ResponseEntity<String> uploadFile(
            @Parameter(description = "File to upload") @RequestParam("file") MultipartFile file) {
        String filename = fileService.uploadFile(file);
        return ResponseEntity.ok("File uploaded successfully: " + filename);
    }

    @GetMapping("/download/{filename}")
    @Operation(summary = "Download a file", description = "Downloads a file from the server")
    public ResponseEntity<Resource> downloadFile(
            @Parameter(description = "Filename to download") @PathVariable String filename) {
        Resource resource = fileService.downloadFile(filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}