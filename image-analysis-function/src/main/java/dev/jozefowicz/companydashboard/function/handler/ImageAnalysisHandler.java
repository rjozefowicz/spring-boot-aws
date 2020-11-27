package dev.jozefowicz.companydashboard.function.handler;

import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import org.springframework.cloud.function.adapter.aws.SpringBootRequestHandler;

public class ImageAnalysisHandler extends SpringBootRequestHandler<S3EventNotification, Void> {
}
