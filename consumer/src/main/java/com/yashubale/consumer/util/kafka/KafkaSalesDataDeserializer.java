package com.yashubale.consumer.util.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import com.yashubale.commons.models.DeserializedRecord;
import com.yashubale.commons.models.SalesEntity;
import com.yashubale.commons.utils.SerDeserUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
public class KafkaSalesDataDeserializer implements Deserializer<DeserializedRecord<SalesEntity>> {

    @Override
    public DeserializedRecord<SalesEntity> deserialize(String s, byte[] bytes) {
        try {
            if (Objects.isNull(bytes)) return DeserializedRecord.createEmptyRecord();
            SalesEntity record = SerDeserUtils.readValue(bytes, new TypeReference<SalesEntity>() {
            });
            return DeserializedRecord.<SalesEntity>builder()
                    .deserializedRecord(record)
                    .build();
        } catch (Exception e) {
            log.error("Exception occurred while deserializing kafka record", e);
            return DeserializedRecord.<SalesEntity>builder()
                    .serializedRecord(new String(bytes, StandardCharsets.UTF_8))
                    .deserializationException(e)
                    .build();
        }
    }
}
