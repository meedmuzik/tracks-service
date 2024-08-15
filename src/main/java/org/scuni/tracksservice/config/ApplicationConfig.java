package org.scuni.tracksservice.config;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.scuni.tracksservice.properties.MinioProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final MinioProperties minioProperties;

    @Bean
    public MinioClient minioClient(){
        return MinioClient.builder()
                .endpoint(minioProperties.getUrl())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
    }

}
