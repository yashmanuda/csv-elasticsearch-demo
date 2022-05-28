package com.yashubale.consumer.service;

import com.yashubale.commons.models.DeserializedRecord;
import com.yashubale.commons.models.SalesEntity;
import com.yashubale.consumer.models.KafkaConfigs;
import com.yashubale.consumer.repository.SalesDataElasticsearchRepository;
import com.yashubale.consumer.util.kafka.KafkaSalesDataDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Slf4j
public class SalesDataKafkaConsumer {

    private final String index;
    private final SalesDataElasticsearchRepository salesDataElasticsearchRepository;
    private final KafkaConfigs kafkaConfigs;

    public SalesDataKafkaConsumer(KafkaConfigs kafkaConfigs,
                                  SalesDataElasticsearchRepository salesDataElasticsearchRepository,
                                  String index) {
        this.index = index;
        this.kafkaConfigs = kafkaConfigs;
        this.salesDataElasticsearchRepository = salesDataElasticsearchRepository;
    }

    private Properties getKafkaConsumerProperties() {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigs.getBootstrapServers());
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaSalesDataDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, kafkaConfigs.getGroupId());
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConfigs.getAutoOffsetReset());
        return properties;
    }

    public void consume() {
        Properties properties = getKafkaConsumerProperties();
        KafkaConsumer<String, DeserializedRecord<SalesEntity>> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList(kafkaConfigs.getTopic()));
        while (true) {
            try {
                ConsumerRecords<String, DeserializedRecord<SalesEntity>> records = consumer.poll(Duration.ofMillis(kafkaConfigs.getMaxPollRecordsMs()));
                List<SalesEntity> salesEntityList = new ArrayList<>();
                for (ConsumerRecord<String, DeserializedRecord<SalesEntity>> record : records) {
                    if (record.value().isDeserializationSuccessful())
                        salesEntityList.add(record.value().getDeserializedRecord());
                }
                if (!salesEntityList.isEmpty()) {
                    salesDataElasticsearchRepository.bulkIndexSalesEntities(salesEntityList, index);
                    log.info("Indexed {} records in ES", salesEntityList.size());
                }
            } catch (Exception e) {
                log.error("Consumer poll failed with exception", e);
            }
        }
    }
}
