package dev.jozefowicz.companydashboardbackend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.core.io.Resource;

import java.time.LocalDate;

public class Attachment {

    private String fileName;
    private LocalDate lastModified;
    private long fileLength;
    private String id;
    @JsonIgnore
    private Resource resource;

    public Attachment(String id, String fileName, LocalDate lastModified, long fileLength, Resource resource) {
        this.fileName = fileName;
        this.lastModified = lastModified;
        this.fileLength = fileLength;
        this.resource = resource;
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public LocalDate getLastModified() {
        return lastModified;
    }

    public long getFileLength() {
        return fileLength;
    }

    public String getId() {
        return id;
    }

    public Resource getResource() {
        return resource;
    }
}
