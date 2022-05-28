package com.yashubale.consumer;

import com.yashubale.consumer.models.ElasticsearchConfigs;
import com.yashubale.consumer.models.KafkaConfigs;
import com.yashubale.consumer.repository.SalesDataElasticsearchRepository;
import com.yashubale.consumer.service.SalesDataKafkaConsumer;
import com.yashubale.consumer.util.elasticsearch.RestHighLevelClientFactory;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.net.URISyntaxException;

@Slf4j
public class ConsumerApplication {
    private static final String index = "sales-index";

    public static void main(String[] args) throws IOException, URISyntaxException {
        SalesDataKafkaConsumer salesDataKafkaConsumer = getSalesDataKafkaConsumer();
        salesDataKafkaConsumer.consume();
    }

    private static SalesDataKafkaConsumer getSalesDataKafkaConsumer() throws IOException, URISyntaxException {
        return new SalesDataKafkaConsumer(
                getKafkaConfigs(),
                getSalesDataRepository(),
                index);
    }

    private static RestHighLevelClient getRestHighLevelClient() {
        ElasticsearchConfigs elasticsearchConfigs = getElasticsearchConfigs();
        return RestHighLevelClientFactory.getSyncClient(elasticsearchConfigs);
    }

    private static SalesDataElasticsearchRepository getSalesDataRepository() throws IOException, URISyntaxException {
        RestHighLevelClient restHighLevelClient = getRestHighLevelClient();
        SalesDataElasticsearchRepository salesDataElasticsearchRepository = new SalesDataElasticsearchRepository(restHighLevelClient);
        boolean indexIfNotExists = salesDataElasticsearchRepository.createIndexIfNotExists(index, "schema.json");
        if (indexIfNotExists) log.info("Index created!");
        return salesDataElasticsearchRepository;
    }

    private static ElasticsearchConfigs getElasticsearchConfigs() {
        return ElasticsearchConfigs
                .builder()
                .scheme("http")
                .host("localhost")
                .port(9200)
                .connectionRequestTimeout(1000)
                .connectTimeout(1000)
                .socketTimeout(1000)
                .build();
    }

    private static KafkaConfigs getKafkaConfigs() {
        return KafkaConfigs.builder()
                .maxPollRecordsMs(1000L)
                .bootstrapServers("localhost:9092")
                .groupId("group-id-v1")
                .topic("sales-data-topic")
                .autoOffsetReset("earliest")
                .build();
    }
}
