package com.yashubale.consumer.util.elasticsearch;

import com.yashubale.consumer.models.ElasticsearchConfigs;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

public class RestHighLevelClientFactory {

    public static RestHighLevelClient getSyncClient(ElasticsearchConfigs elasticsearchConfigs) {
        HttpHost httpHost = new HttpHost(elasticsearchConfigs.getHost(), elasticsearchConfigs.getPort(), elasticsearchConfigs.getScheme());
        RestClientBuilder restClientBuilder = RestClient
                .builder(httpHost)
                .setRequestConfigCallback(
                        builder -> builder
                                .setConnectTimeout(elasticsearchConfigs.getConnectTimeout())
                                .setSocketTimeout(elasticsearchConfigs.getSocketTimeout())
                                .setConnectionRequestTimeout(elasticsearchConfigs.getConnectionRequestTimeout())
                );
        return new RestHighLevelClient(restClientBuilder);
    }

}
