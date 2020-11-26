package dev.jozefowicz.companydashboardbackend.domain;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class Note {

    private String noteId;
    private String title;
    private String body;
    private long timestamp;
    private NoteSentiment sentiment;

    @DynamoDbPartitionKey
    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public NoteSentiment getSentiment() {
        return sentiment;
    }

    public void setSentiment(NoteSentiment sentiment) {
        this.sentiment = sentiment;
    }
}
