package com.imageanalysis.api.config;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.client.RestClient;

@Configuration
public class ImageProcessorConfiguration {

    @Value("${pubsub.subscription}")
    private String subscription;

    @Bean
    public RestClient restClient(@Value("${persistent.service.address}") String persistentUrl) {
        return RestClient.builder().baseUrl(persistentUrl).build();
    }

    @Bean
    public PubSubInboundChannelAdapter messageChannelAdapter(MessageChannel pubsubChannel,
                                                             PubSubTemplate pubSubTemplate) {
        final var adapter = new PubSubInboundChannelAdapter(pubSubTemplate, subscription);
        adapter.setOutputChannel(pubsubChannel);
        return adapter;
    }

    @Bean
    public MessageChannel pubsubChannel() {
        return new DirectChannel();
    }
}
