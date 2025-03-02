package com.imageanalysis.api.dto;

import java.util.UUID;

public record ImageProcessStatusDto(
    UUID imageProcessId,
    String status
) {
}
