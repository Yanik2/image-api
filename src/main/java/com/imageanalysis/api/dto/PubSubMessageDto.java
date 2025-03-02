package com.imageanalysis.api.dto;

import java.util.UUID;

public record PubSubMessageDto(
    String status,
    UUID processingId
) {
}
