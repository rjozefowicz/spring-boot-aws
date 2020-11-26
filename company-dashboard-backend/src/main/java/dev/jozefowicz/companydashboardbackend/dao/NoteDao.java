package dev.jozefowicz.companydashboardbackend.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import dev.jozefowicz.companydashboardbackend.domain.Note;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NoteDao {

    private final AmazonDynamoDB dynamoDB;
    private final DynamoDBMapper mapper;

    public NoteDao(AmazonDynamoDB dynamoDB, @Value("${notes-table}") String tableName) {
        this.dynamoDB = dynamoDB;
        DynamoDBMapperConfig mapperConfig = new DynamoDBMapperConfig.Builder().withTableNameOverride(DynamoDBMapperConfig.TableNameOverride.withTableNameReplacement(tableName))
                .build();
        this.mapper = new DynamoDBMapper(dynamoDB, mapperConfig);
    }

    public void persist(final Note note) {
        mapper.save(note);
    }

    public void delete(String noteId) {
        mapper.delete(findByNoteId(noteId));
    }

    public Note findByNoteId(String noteId) {
        return mapper.load(Note.class, noteId);
    }

    public List<Note> findAll() {
        return mapper.scan(Note.class, new DynamoDBScanExpression());
    }

}
