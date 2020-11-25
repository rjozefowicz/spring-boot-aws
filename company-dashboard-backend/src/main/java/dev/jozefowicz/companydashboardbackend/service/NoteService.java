package dev.jozefowicz.companydashboardbackend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.jozefowicz.companydashboardbackend.dao.NoteDao;
import dev.jozefowicz.companydashboardbackend.domain.Note;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NoteService {

    private final NoteDao noteDao;
    private final NoteAnalysisService noteAnalysisService;



    public NoteService(NoteDao noteDao, NoteAnalysisService noteAnalysisService) {
        this.noteDao = noteDao;
        this.noteAnalysisService = noteAnalysisService;
    }

    public void add(final Note note) throws JsonProcessingException {
        note.setNoteId(UUID.randomUUID().toString());
        note.setTimestamp(System.currentTimeMillis());
        noteDao.persist(note);
        noteAnalysisService.analyse(note);
    }

    public void delete(String noteId) {
        noteDao.delete(noteId);
    }

    public Note findByNoteId(final String noteId) throws JsonProcessingException {
        return noteDao.findByNoteId(noteId);
    }

    public List<Note> findAll() {
        return  noteDao.findAll();
    }
}
