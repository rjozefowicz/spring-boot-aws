package dev.jozefowicz.companydashboardbackend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.jozefowicz.companydashboardbackend.dao.NoteDao;
import dev.jozefowicz.companydashboardbackend.domain.Note;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NoteService {

    private final NoteDao noteDao;

    public NoteService(NoteDao noteDao) {
        this.noteDao = noteDao;
    }

    public void add(Note note) throws JsonProcessingException {
        note.setNoteId(UUID.randomUUID().toString());
        note.setTimestamp(System.currentTimeMillis());
        noteDao.persist(note);
    }
}
