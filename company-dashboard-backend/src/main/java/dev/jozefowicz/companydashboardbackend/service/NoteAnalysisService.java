package dev.jozefowicz.companydashboardbackend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.jozefowicz.companydashboardbackend.dao.NoteDao;
import dev.jozefowicz.companydashboardbackend.domain.Note;
import dev.jozefowicz.companydashboardbackend.domain.NoteAnalysisRequest;
import dev.jozefowicz.companydashboardbackend.domain.NoteAnalysisResponse;
import dev.jozefowicz.companydashboardbackend.domain.NoteSentiment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@Component
public class NoteAnalysisService {

    private final QueueMessagingTemplate queueMessagingTemplate;
    private final NoteDao noteDao;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final NotificationMessagingTemplate notificationMessagingTemplate;

    @Value("${request-queue-name}")
    private String requestQueueName;

    @Value("${admin-notification-topic-name}")
    private String adminNotificationTopicName;

    public NoteAnalysisService(QueueMessagingTemplate queueMessagingTemplate, NoteDao noteDao, SimpMessagingTemplate simpMessagingTemplate, NotificationMessagingTemplate notificationMessagingTemplate) {
        this.queueMessagingTemplate = queueMessagingTemplate;
        this.noteDao = noteDao;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.notificationMessagingTemplate = notificationMessagingTemplate;
    }

    @SqsListener(value = "${response-queue-name}", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void response(NoteAnalysisResponse response) throws JsonProcessingException {
        final Note note = noteDao.findByNoteId(response.getNoteId());
        note.setSentiment(response.getSentiment());
        noteDao.persist(note);
        if (note.getSentiment() == NoteSentiment.NEGATIVE) {
            simpMessagingTemplate.convertAndSend("/topic/news", note);
            notificationMessagingTemplate.sendNotification(adminNotificationTopicName, prepareEmailMessage(note), "Note with negative sentiment");
        }
    }

    public void analyse(final Note note) {
        NoteAnalysisRequest request = new NoteAnalysisRequest();
        request.setBody(note.getBody());
        request.setNoteId(note.getNoteId());
        Map<String, Object> headers = new HashMap<>();
        headers.put("message-group-id", note.getNoteId());
        headers.put("message-deduplication-id", note.getNoteId());
        queueMessagingTemplate.convertAndSend(requestQueueName, request, headers);
    }

    private String prepareEmailMessage(final Note note) {
        return String.format("Note with negative sentiment: %s, from %s", note.getBody(), Instant.ofEpochMilli(note.getTimestamp()).atZone(ZoneId.systemDefault()).toString());
    }

}
