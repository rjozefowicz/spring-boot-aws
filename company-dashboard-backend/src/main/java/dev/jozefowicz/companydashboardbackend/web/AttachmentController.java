package dev.jozefowicz.companydashboardbackend.web;

import dev.jozefowicz.companydashboardbackend.domain.Attachment;
import dev.jozefowicz.companydashboardbackend.service.AttachmentService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/attachments")
public class AttachmentController {

    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @CrossOrigin(origins = "*")
    @GetMapping
    public List<Attachment> listAll() throws IOException {
        return attachmentService.listAll();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public ResponseEntity<Resource> download(@PathVariable String id) throws IOException {

        final Optional<Attachment> attachment = attachmentService.getById(id);

        if (attachment.isPresent()) {
            final Resource resource = attachment.get().getResource();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=%s", attachment.get().getFileName()));
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(attachment.get().getFileLength())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping
    public void upload(@RequestParam("file") MultipartFile file) throws IOException {
        attachmentService.upload(file.getBytes(), file.getOriginalFilename());
    }
}
