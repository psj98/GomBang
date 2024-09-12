package com.ssafy.elasticsearch.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {

    @Override
    public RestHighLevelClient elasticsearchClient() {

        // http port와 통신할 주소
        ClientConfiguration configuration = ClientConfiguration.builder().connectedTo("i9a804.p.ssafy.io:9200").build();
//        System.out.println("Elastic Search 연결 완료");
        return RestClients.create(configuration).rest();
    }
}
