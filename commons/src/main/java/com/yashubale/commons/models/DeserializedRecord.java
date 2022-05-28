package com.yashubale.commons.models;

import com.yashubale.commons.exceptions.NullRecordException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeserializedRecord<T> {
    private T deserializedRecord;
    private String serializedRecord;
    private Exception deserializationException;

    public static <T> DeserializedRecord<T> createEmptyRecord() {
        return new DeserializedRecord<>(null, null, new NullRecordException("Null record!"));
    }

    public boolean isDeserializationSuccessful() {
        return deserializedRecord != null;
    }
}

