package dev.jozefowicz.companydashboard.function.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsRequest;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsResponse;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.rekognition.model.S3Object;

@Service
public class ImageAnalysisService {
    private final RekognitionClient rekognitionClient;

    public ImageAnalysisService(RekognitionClient rekognitionClient) {
        this.rekognitionClient = rekognitionClient;
    }

    public void analyse(String bucketName, String key) {
        final DetectLabelsResponse res = rekognitionClient.detectLabels(DetectLabelsRequest.builder()
                .image(
                        Image.builder().s3Object(
                                S3Object.builder().bucket(bucketName).name(key).build()
                        ).build())
                .maxLabels(10)
                .minConfidence(0.75f)
                .build());

        // TODO process results properly
        res.labels().forEach(label -> System.out.println(label.name()));
    }
}
