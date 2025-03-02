package com.imageanalysis.api.dto;

import java.util.UUID;

public record ConfirmationResponseDto(
    UUID imageProcessId
) {
}
