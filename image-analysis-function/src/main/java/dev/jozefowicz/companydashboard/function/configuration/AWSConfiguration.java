package dev.jozefowicz.companydashboard.function.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.rekognition.RekognitionClient;

@Configuration
public class AWSConfiguration {

    @Bean
    public RekognitionClient rekognitionClient() {
        return RekognitionClient.create();
    }

}
