package com.yashubale.consumer.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ElasticsearchConfigs {
    private String host;
    private Integer port;
    private String scheme;
    private String index;
    private Integer connectTimeout;
    private Integer socketTimeout;
    private Integer connectionRequestTimeout;
}
