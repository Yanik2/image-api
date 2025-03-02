package com.imageanalysis.api.service;

import com.imageanalysis.api.dto.ConfirmUploadDto;
import com.imageanalysis.api.dto.ConfirmationResponseDto;
import com.imageanalysis.api.dto.CreateProcessRequest;
import com.imageanalysis.api.dto.CreateProcessResponse;
import com.imageanalysis.api.dto.ImageProcessStatusDto;
import com.imageanalysis.api.dto.PubSubMessageDto;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class ImageProcessPersistentService {
    private static final String PROCESS_STATUS_URI_TEMPLATE = "/process/%s/status";

    private final RestClient restClient;

    public CreateProcessResponse createProcess(CreateProcessRequest request) {
        return restClient.post()
            .body(request)
            .contentType(MediaType.APPLICATION_JSON)
            .retrieve()
            .body(CreateProcessResponse.class);
    }

    public ConfirmationResponseDto confirmUpload(ConfirmUploadDto request) {
        return restClient.put()
            .uri("/process/%s/confirm".formatted(request.processId()))
            .body(request)
            .contentType(MediaType.APPLICATION_JSON)
            .retrieve()
            .body(ConfirmationResponseDto.class);
    }

    public ImageProcessStatusDto getStatus(UUID imageProcessId) {
        return restClient.get()
            .uri(URI.create(PROCESS_STATUS_URI_TEMPLATE.formatted(imageProcessId)))
            .retrieve()
            .body(ImageProcessStatusDto.class);
    }

    public void updateStatus(PubSubMessageDto message) {
        restClient.put()
            .uri(PROCESS_STATUS_URI_TEMPLATE.formatted(message.processingId()))
            .body(message)
            .contentType(MediaType.APPLICATION_JSON)
            .retrieve()
            .toBodilessEntity();
    }
}
