package com.imageanalysis.api.pubsub;

import com.imageanalysis.api.dto.PubSubMessageDto;
import com.imageanalysis.api.service.ImageProcessPersistentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PubSubSubscriber {
    private final ImageProcessPersistentService imageService;

    @ServiceActivator(inputChannel = "pubsubChannel")
    public void receiveMessage(Message<PubSubMessageDto> message) {
        final var payload = message.getPayload();
        log.info("Received message: {}", payload);
        imageService.updateStatus(payload);
    }
}
