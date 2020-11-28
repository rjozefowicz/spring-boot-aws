package dev.jozefowicz.companydashboardbackend.service;

import dev.jozefowicz.companydashboardbackend.domain.Attachment;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AttachmentService {

    public List<Attachment> listAll() {
        return Collections.emptyList();
    }

    public Optional<Attachment> getById(final String id) {
        return  Optional.empty();
    }

    private Attachment mapToAttachment(Resource resource) {
        try {
            final String[] keyParts = resource.getFilename().split("/");
            if (keyParts.length != 2) {
                return null;
            }
            return new Attachment(keyParts[0], keyParts[1],
                    Instant.ofEpochMilli(resource.lastModified()).atZone(ZoneId.systemDefault()).toLocalDate(),
                    resource.contentLength(), resource);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void upload(byte[] file, String fileName) throws IOException {
        // TODO
    }

    public void delete(final String id) throws IOException {
        // TODO
    }
}
