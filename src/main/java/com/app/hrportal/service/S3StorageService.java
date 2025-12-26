package com.app.hrportal.service;

import com.app.hrportal.exception.ValidationException;
import com.app.hrportal.exception.application.StorageException;
import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3StorageService implements StorageService {

    private final S3Template s3Template;

    @Value("${app.aws.s3.bucket}")
    private String bucketName;

    @Override
    public String uploadResume(
            MultipartFile file,
            String jobId,
            String applicationId
    ) {
        validateFile(file);

        String extension = getFileExtension(file.getOriginalFilename());

        String objectKey = String.format(
                "resumes/%s/%s.%s",
                jobId,
                applicationId,
                extension
        );

        try (InputStream inputStream = file.getInputStream()) {

            s3Template.upload(
                    bucketName,
                    objectKey,
                    inputStream,
                    ObjectMetadata.builder()
                            .contentType(file.getContentType())
                            .contentLength(file.getSize())
                            .build()
            );

            return objectKey;

        } catch (Exception e) {
            log.info("Failed while uploading to s3, {}",e.getMessage());
            throw new StorageException("Failed to upload resume to S3", e);
        }
    }

    @Override
    public InputStream getResume(String resumeKey) {
        try {
            return s3Template.download(bucketName, resumeKey).getInputStream();
        } catch (Exception e) {
            throw new StorageException("Failed to download resume from S3", e);
        }
    }


    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ValidationException("Resume file is empty");
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new ValidationException("Resume file size exceeds 5MB");
        }

        List<String> allowedTypes = List.of(
                "application/pdf"
        );

        if (!allowedTypes.contains(file.getContentType())) {
            throw new ValidationException("Unsupported resume format");
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "pdf";
        }
        return fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
    }
}