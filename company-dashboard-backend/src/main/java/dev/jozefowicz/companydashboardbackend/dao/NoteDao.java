package dev.jozefowicz.companydashboardbackend.dao;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jozefowicz.companydashboardbackend.domain.Note;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NoteDao {

    private final DynamoDB dynamoDB;
    private final Table table;
    private final ObjectMapper objectMapper;

    public NoteDao(DynamoDB dynamoDB, @Value("${notes-table}") String table, ObjectMapper objectMapper) {
        this.dynamoDB = dynamoDB;
        this.objectMapper = objectMapper;
        this.table = dynamoDB.getTable(table);
    }

    public void persist(Note note) throws JsonProcessingException {
        table.putItem(Item.fromJSON(objectMapper.writeValueAsString(note)));
    }

    public void delete(String noteId) {
        table.deleteItem("noteId", noteId);
    }

    public Note findByNoteId(String noteId) throws JsonProcessingException {
        final Item item = table.getItem("noteId", noteId);
        return objectMapper.readValue(item.toJSON(), Note.class);
    }

    public List<Note> findAll() {
        final List<Note> notes = new ArrayList<>();
        table.scan().forEach(item -> {
            try {
                notes.add(objectMapper.readValue(item.toJSON(), Note.class));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        return notes;
    }
}
