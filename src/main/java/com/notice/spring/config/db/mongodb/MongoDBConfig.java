package com.notice.spring.config.db.mongodb;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

import java.util.Arrays;

/**
 * <pre>
 *     MongoDB 설정
 * </pre>
 */
@Configuration
public class MongoDBConfig {
    private final String host;
    private final int port;
    private final String userName;
    private final String password;
    private final String authDatabase;

    public MongoDBConfig(@Value("${spring.mongodb.host}")String host
            , @Value("${spring.mongodb.port}")int port
            , @Value("${spring.mongodb.username}")String userName
            , @Value("${spring.mongodb.password}")String password
            , @Value("${spring.mongodb.auth-database}")String authDatabase) {

        this.host = host;
        this.port = port;
        this.userName = userName;
        this.password = password;
        this.authDatabase = authDatabase;
    }

    @Bean
    public MongoClient mongoClient() {

        MongoCredential credential = MongoCredential.createCredential(this.userName
                , this.authDatabase
                , this.password.toCharArray());

        MongoClientSettings settings = MongoClientSettings.builder().credential(credential)
                .applyToClusterSettings(builder -> builder
                        .hosts(Arrays.asList(new ServerAddress(this.host, this.port))))
                .build();

        return MongoClients.create(settings);
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {

        MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, "Notice");
        ((MappingMongoConverter) mongoTemplate.getConverter()).setTypeMapper(new DefaultMongoTypeMapper(null));

        return mongoTemplate;
    }

}
