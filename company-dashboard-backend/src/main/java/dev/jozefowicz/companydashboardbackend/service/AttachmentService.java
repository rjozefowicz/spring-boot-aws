package dev.jozefowicz.companydashboardbackend.service;

import com.amazonaws.services.s3.AmazonS3;
import dev.jozefowicz.companydashboardbackend.domain.Attachment;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AttachmentService {

    private final ResourceLoader resourceLoader;

    @Value("${attachment.bucket-name}")
    private String bucketName;

    private ResourcePatternResolver resourcePatternResolver;

    @Autowired
    public void setupResolver(ApplicationContext applicationContext, AmazonS3 amazonS3){
        this.resourcePatternResolver = new PathMatchingSimpleStorageResourcePatternResolver(amazonS3, applicationContext);
    }

    public AttachmentService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public List<Attachment> listAll() throws IOException {
        final Resource[] resources = resourcePatternResolver.getResources(String.format("s3://%s/**/*", bucketName));
        return Arrays.asList(resources)
                .stream()
                .map(this::mapToAttachment)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public Optional<Attachment> getById(final String id) throws IOException {
        Resource[] resources = resourcePatternResolver.getResources(String.format("s3://%s/%s/*", bucketName, id));
        final Resource resource = resources[0];
        return resource.exists() ? Optional.ofNullable(mapToAttachment(resource)) : Optional.empty();
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
        Resource resource = this.resourceLoader.getResource(String.format("s3://%s/%s/%s", bucketName, UUID.randomUUID().toString(), fileName));
        WritableResource writableResource = (WritableResource) resource;
        try (OutputStream outputStream = writableResource.getOutputStream()) {
            outputStream.write(file);
        }
    }
}
