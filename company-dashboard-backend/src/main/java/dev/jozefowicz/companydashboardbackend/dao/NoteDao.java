package dev.jozefowicz.companydashboardbackend.dao;

import dev.jozefowicz.companydashboardbackend.domain.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NoteDao {

    private final DynamoDbTable<Note> table;

    public NoteDao(@Autowired DynamoDbEnhancedClient client, @Value("${notes-table}") String tableName) {
        this.table =
                client.table(tableName, TableSchema.fromBean(Note.class));
    }

    public void persist(final Note note) {
        table.putItem(note);
    }

    public void delete(String noteId) {
        table.deleteItem(Key.builder().partitionValue(noteId).build());
    }

    public Note findByNoteId(String noteId) {
        return table.getItem(Key.builder().partitionValue(noteId).build());
    }

    public List<Note> findAll() {
        return table.scan().items().stream().collect(Collectors.toList());
    }

}
