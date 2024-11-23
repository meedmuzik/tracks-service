package org.scuni.tracksservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties
@EnableFeignClients
@EnableNeo4jRepositories(basePackages = "org.scuni.tracksservice.repository")
@EnableTransactionManagement
public class TracksServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TracksServiceApplication.class, args);
    }

}
