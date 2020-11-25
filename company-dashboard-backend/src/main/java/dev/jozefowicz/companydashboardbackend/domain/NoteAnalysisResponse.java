package dev.jozefowicz.companydashboardbackend.domain;

public class NoteAnalysisResponse {

    private String noteId;
    private NoteSentiment sentiment;

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public NoteSentiment getSentiment() {
        return sentiment;
    }

    public void setSentiment(NoteSentiment sentiment) {
        this.sentiment = sentiment;
    }
}
