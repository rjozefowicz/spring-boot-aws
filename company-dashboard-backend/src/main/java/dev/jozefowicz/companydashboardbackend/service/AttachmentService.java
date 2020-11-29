package dev.jozefowicz.companydashboardbackend.service;

import com.amazonaws.services.s3.AmazonS3;
import dev.jozefowicz.companydashboardbackend.domain.Attachment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.core.io.s3.PathMatchingSimpleStorageResourcePatternResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AttachmentService {

    private final ResourceLoader resourceLoader;
    private final ResourcePatternResolver resourcePatternResolver;
    private final AmazonS3 amazonS3;

    @Value("${attachment.bucket-name}")
    private String bucketName;

    public AttachmentService(ResourceLoader resourceLoader, AmazonS3 amazonS3, ApplicationContext applicationContext) {
        this.resourceLoader = resourceLoader;
        this.amazonS3 = amazonS3;
        this.resourcePatternResolver = new PathMatchingSimpleStorageResourcePatternResolver(amazonS3, applicationContext);
    }

    public List<Attachment> listAll() throws IOException {
        final Resource[] resources = resourcePatternResolver.getResources(String.format("s3://%s/**/*", bucketName));
        return Arrays.asList(resources).stream().map(this::mapToAttachment).collect(Collectors.toList());
    }

    public Optional<Attachment> getById(final String id) throws IOException {
        final Resource[] resources = resourcePatternResolver.getResources(String.format("s3://%s/%s/*", bucketName, id));
        return Optional.of(mapToAttachment(resources[0]));
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
        final Resource resource = resourceLoader.getResource(String.format("s3://%s/%s/%s", bucketName, UUID.randomUUID().toString(), fileName));
        WritableResource writableResource = (WritableResource) resource;
        try (OutputStream outputStream = writableResource.getOutputStream()) {
            outputStream.write(file);
        }
    }

    public void delete(final String id) throws IOException {
        getById(id).ifPresent(attachment -> {
            amazonS3.deleteObject(bucketName, String.format("%s/%s", id, attachment.getFileName()));
        });
    }
}
