package dev.jozefowicz.companydashboard.function;

public class NoteAnalysisResponse {

    private String noteId;
    private String sentiment;

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }
}
