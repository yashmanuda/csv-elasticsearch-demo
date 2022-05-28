package com.yashubale.producer.connectors;

import com.yashubale.commons.filesource.csv.CsvReader;
import com.yashubale.commons.models.FileSourceParams;
import com.yashubale.commons.utils.kafka.GenericSerializationSchema;
import com.yashubale.producer.configs.ProducerApplicationConfig;
import com.yashubale.commons.models.SalesEntity;
import lombok.Getter;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.file.src.FileSource;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;

@Getter
public class ProducerConnectors {
    private final FileSource<SalesEntity> salesEntityFileSource;
    private final KafkaSink<SalesEntity> salesEntitySink;

    public ProducerConnectors(ProducerApplicationConfig producerApplicationConfig) {
        FileSourceParams fileSourceParams = getFileSourceParams(producerApplicationConfig);
        salesEntityFileSource = CsvReader.getCsvFileSourceFromPath(fileSourceParams, SalesEntity.class);
        salesEntitySink = KafkaSink.<SalesEntity>builder()
                .setBootstrapServers(producerApplicationConfig.getKafkaBootstrapServers())
                .setRecordSerializer(KafkaRecordSerializationSchema.builder()
                        .setTopic(producerApplicationConfig.getSinkTopic())
                        .setValueSerializationSchema(new GenericSerializationSchema<SalesEntity>())
                        .build()
                )
                .setDeliverGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
                .build();
    }

    private FileSourceParams getFileSourceParams(ProducerApplicationConfig producerApplicationConfig) {
        return FileSourceParams
                .builder()
                .path(producerApplicationConfig.getFileSourcePath())
                .sourceType(FileSourceParams.SourceType.UNBOUNDED)
                .seconds(5L)
                .build();
    }
}
