package com.yashubale.consumer.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.yashubale.commons.models.SalesEntity;
import com.yashubale.commons.utils.SerDeserUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.ActiveShardCount;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;

@Slf4j
public class SalesDataElasticsearchRepository {

    private final RestHighLevelClient restHighLevelClient;

    public SalesDataElasticsearchRepository(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    public boolean createIndexIfNotExists(String index, String indexSchemaPath) throws IOException, URISyntaxException {
        GetIndexRequest getIndexRequest = new GetIndexRequest(index);
        if (!restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT)) {
            log.info("Index - {} not present, creating the index", index);
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
            Map<String, Object> indexSchema = getIndexSchema(indexSchemaPath);
            createIndexRequest.waitForActiveShards(ActiveShardCount.ONE);
            createIndexRequest.source(SerDeserUtils.writeValueAsString(indexSchema), XContentType.JSON);
            CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            return createIndexResponse.isAcknowledged();
        }
        return false;
    }

    private IndexRequest getIndexRequest(SalesEntity salesEntity, String index) throws JsonProcessingException {
        return new IndexRequest()
                .index(index)
                .id(String.valueOf(salesEntity.getOrderId()))
                .waitForActiveShards(ActiveShardCount.ONE)
                .source(SerDeserUtils.writeValueAsString(salesEntity), XContentType.JSON);
    }

    public void indexSalesEntity(SalesEntity salesEntity, String index) throws IOException {
        IndexRequest indexRequest = getIndexRequest(salesEntity, index);
        restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
    }

    public BulkResponse bulkIndexSalesEntities(List<SalesEntity> salesEntityList, String index) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        for (SalesEntity salesEntity : salesEntityList) {
            IndexRequest indexRequest;
            try {
                indexRequest = getIndexRequest(salesEntity, index);
                bulkRequest.add(indexRequest);
            } catch (JsonProcessingException e) {
                log.error("Failed creating index request", e);
            }
        }
        return restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
    }

    private Map<String, Object> getIndexSchema(String indexSchemaFileName) throws IOException, URISyntaxException {
        File file = getFileFromResource(indexSchemaFileName);
        return SerDeserUtils.readValue(file, new TypeReference<Map<String, Object>>() {
        });
    }

    private File getFileFromResource(String fileName) throws URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) throw new IllegalArgumentException("file not found! " + fileName);
        else return new File(resource.toURI());

    }
}
