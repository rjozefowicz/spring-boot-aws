package dev.jozefowicz.companydashboard.function;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.comprehend.ComprehendClient;
import software.amazon.awssdk.services.comprehend.model.DetectSentimentRequest;
import software.amazon.awssdk.services.comprehend.model.DetectSentimentResponse;
import software.amazon.awssdk.services.comprehend.model.LanguageCode;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

public class NoteAnalysisHandler implements RequestHandler<SQSEvent, Void> {

    private final static String RESPONSE_QUEUE_URL = System.getenv("RESPONSE_QUEUE_URL");
    private final static Region REGION = Region.of(System.getenv("AWS_REGION"));

    private final ComprehendClient comprehendClient = ComprehendClient.builder()
            .region(REGION)
            .build();

    private final SqsClient sqsClient = SqsClient.builder()
            .region(REGION)
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Void handleRequest(SQSEvent sqsEvent, Context context) {
        sqsEvent.getRecords().forEach(record -> {
            try {
                NoteAnalysisRequest noteAnalysisRequest = objectMapper.readValue(record.getBody(), NoteAnalysisRequest.class);

                DetectSentimentRequest sentimentRequest = DetectSentimentRequest
                        .builder().languageCode(LanguageCode.EN).text(noteAnalysisRequest.getBody()).build();

                final DetectSentimentResponse sentimentResponse = comprehendClient.detectSentiment(sentimentRequest);

                NoteAnalysisResponse response = new NoteAnalysisResponse();
                response.setNoteId(noteAnalysisRequest.getNoteId());
                response.setSentiment(sentimentResponse.sentimentAsString());

                SendMessageRequest messageRequest = SendMessageRequest
                        .builder()
                        .messageDeduplicationId(noteAnalysisRequest.getNoteId())
                        .messageGroupId(noteAnalysisRequest.getNoteId())
                        .messageBody(objectMapper.writeValueAsString(response))
                        .queueUrl(RESPONSE_QUEUE_URL).build();
                sqsClient.sendMessage(messageRequest);

            } catch (Exception e) {
                e.printStackTrace();
                context.getLogger().log(String.format("Unable to analyse note %s", record.getBody()));
            }
        });
        return null;
    }
}
