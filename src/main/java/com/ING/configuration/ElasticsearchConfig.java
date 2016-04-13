package com.ING.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

/**
 * Created by renuevo on 2016-04-13.
 */
@Configuration
public class ElasticsearchConfig {

    @Bean
    ElasticsearchOperations elasticsearchTemplate() throws Exception {

        return new ElasticsearchTemplate(ElasticsearchClient.shareClient());
    }
}
