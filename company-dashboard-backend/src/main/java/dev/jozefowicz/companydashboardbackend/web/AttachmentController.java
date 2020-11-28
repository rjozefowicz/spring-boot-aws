package dev.jozefowicz.companydashboardbackend.web;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/attachments")
public class AttachmentController {

    @CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public ResponseEntity<Resource> download(@PathVariable String id) throws IOException {
        return ResponseEntity.notFound().build(); // TODO
    }

}
