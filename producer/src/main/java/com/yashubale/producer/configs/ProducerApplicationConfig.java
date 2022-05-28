package com.yashubale.producer.configs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProducerApplicationConfig {
    private String fileSourcePath;
    private String sinkTopic;
    private String kafkaBootstrapServers;
}
