package com.yashubale.consumer.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class KafkaConfigs {
    private String bootstrapServers;
    private String groupId;
    private String topic;
    private String autoOffsetReset;
    private Long maxPollRecordsMs;
}
