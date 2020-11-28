package dev.jozefowicz.companydashboardbackend.service;

import dev.jozefowicz.companydashboardbackend.domain.Note;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;

@Component
public class NoteAnalysisService {

    private String prepareEmailMessage(final Note note) {
        return String.format("Note with negative sentiment: %s, from %s", note.getBody(), Instant.ofEpochMilli(note.getTimestamp()).atZone(ZoneId.systemDefault()).toString());
    }

}
