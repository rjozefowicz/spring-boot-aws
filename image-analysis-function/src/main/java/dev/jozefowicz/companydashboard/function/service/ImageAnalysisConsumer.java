package dev.jozefowicz.companydashboard.function.service;

import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class ImageAnalysisConsumer implements Consumer<S3EventNotification> {

    private final ImageAnalysisService imageAnalysisService;

    public ImageAnalysisConsumer(ImageAnalysisService imageAnalysisService) {
        this.imageAnalysisService = imageAnalysisService;
    }

    @Override
    public void accept(S3EventNotification s3EventNotification) {
        s3EventNotification
                .getRecords()
                .forEach(record -> imageAnalysisService.analyse(record.getS3().getBucket().getName(), record.getS3().getObject().getKey()));

    }
}
