package dev.jozefowicz.companydashboardbackend.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.jozefowicz.companydashboardbackend.domain.Note;
import dev.jozefowicz.companydashboardbackend.service.NoteService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @CrossOrigin(origins = "*")
    @PostMapping
    public void add(@RequestBody  Note note) throws JsonProcessingException {
        noteService.add(note);
    }

    @CrossOrigin(origins = "*")
    @GetMapping
    public List<Note> findAll() {
        return noteService.findAll();
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/{noteId}")
    public void delete(@PathVariable String noteId) {
        noteService.delete(noteId);
    }

}
