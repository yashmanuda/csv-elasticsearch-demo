package com.yashubale.commons.utils.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yashubale.commons.utils.SerDeserUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.serialization.SerializationSchema;

@Slf4j
public class GenericSerializationSchema<T> implements SerializationSchema<T> {

    @Override
    public void open(InitializationContext context) throws Exception {
        SerializationSchema.super.open(context);
    }

    @Override
    public byte[] serialize(T t) {
        try {
            return SerDeserUtils.writeValueAsBytes(t);
        } catch (JsonProcessingException e) {
            log.error("Exception occurred while serializing data!", e);
        }
        return null;
    }
}
