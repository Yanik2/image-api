package com.imageanalysis.api.controller;

import com.imageanalysis.api.dto.ConfirmUploadDto;
import com.imageanalysis.api.dto.ConfirmationResponseDto;
import com.imageanalysis.api.dto.CreateProcessRequest;
import com.imageanalysis.api.dto.CreateProcessResponse;
import com.imageanalysis.api.dto.ImageProcessStatusDto;
import com.imageanalysis.api.dto.ImageUrlDto;
import com.imageanalysis.api.service.GcsClient;
import com.imageanalysis.api.service.ImageProcessPersistentService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController {
    private final ImageProcessPersistentService imageService;
    private final GcsClient gcsClient;

    @PostMapping
    public ResponseEntity<CreateProcessResponse> createImageProcess(
        @RequestBody CreateProcessRequest request
    ) {
        return ResponseEntity.ok(imageService.createProcess(request));
    }

    @GetMapping("/{imageId}/upload-link")
    public ResponseEntity<ImageUrlDto> getUploadLink(@PathVariable UUID imageId) {
        final var uploadLink = gcsClient.getUploadLink(imageId.toString());
        return ResponseEntity.ok(new ImageUrlDto(uploadLink));
    }

    @PostMapping("/{imageId}/confirm")
    public ResponseEntity<ConfirmationResponseDto> confirm(@PathVariable UUID imageId) {
        return ResponseEntity.ok(imageService.confirmUpload(new ConfirmUploadDto(imageId)));
    }

    @GetMapping("/{imageProcessId}/status")
    public ResponseEntity<ImageProcessStatusDto> getStatus(@PathVariable UUID imageProcessId) {
        return ResponseEntity.ok(imageService.getStatus(imageProcessId));
    }

    @GetMapping("/{imageId}/download-link")
    public ResponseEntity<ImageUrlDto> getDownloadLink(@PathVariable UUID imageId) {
        return ResponseEntity.ok(
            new ImageUrlDto(gcsClient.getDownloadLink(imageId.toString())));
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable UUID imageId) {
        gcsClient.deleteImage(imageId.toString());
        return ResponseEntity.noContent().build();
    }
}
