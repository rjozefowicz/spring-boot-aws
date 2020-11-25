package dev.jozefowicz.companydashboardbackend.domain;

public class NoteAnalysisRequest {
    private String noteId;
    private String body;

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
