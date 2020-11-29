package dev.jozefowicz.companydashboardbackend.dao;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jozefowicz.companydashboardbackend.domain.Note;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NoteDao {

    private final DynamoDB dynamoDB;
    private final Table table;
    private final ObjectMapper objectMapper;

    public NoteDao(DynamoDB dynamoDB, @Value("${notes.table}") String tableName, ObjectMapper objectMapper) {
        this.dynamoDB = dynamoDB;
        this.objectMapper = objectMapper;
        this.table = dynamoDB.getTable(tableName);
    }

    public void persist(Note note) throws JsonProcessingException {
        table.putItem(Item.fromJSON(objectMapper.writeValueAsString(note)));
    }
}
