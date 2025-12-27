package com.app.hrportal.service;

import com.app.hrportal.exception.ValidationException;
import com.app.hrportal.exception.application.StorageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupabaseStorageService implements StorageService {

    private final WebClient supabaseWebClient;

    @Value("${supabase.bucket}")
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

        try {
            supabaseWebClient
                    .post()
                    .uri("/storage/v1/object/{bucket}/{path}",
                            bucketName,
                            objectKey
                    )
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .bodyValue(file.getBytes())
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::isError,
                            response -> response.bodyToMono(String.class)
                                    .map(msg -> new StorageException(
                                            "Supabase upload failed: " + msg
                                    ))
                    )
                    .toBodilessEntity()
                    .block();

            return objectKey;

        } catch (Exception e) {
            log.error("Failed to upload resume to Supabase", e);
            throw new StorageException("Failed to upload resume", e);
        }
    }

    @Override
    public InputStream getResume(String resumeKey) {
        try {
            return supabaseWebClient
                    .get()
                    .uri("/storage/v1/object/{bucket}/{path}",
                            bucketName,
                            resumeKey
                    )
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::isError,
                            response -> response.bodyToMono(String.class)
                                    .map(msg -> new StorageException(
                                            "Supabase download failed: " + msg
                                    ))
                    )
                    .bodyToMono(byte[].class)
                    .map(ByteArrayInputStream::new)
                    .block();

        } catch (Exception e) {
            throw new StorageException("Failed to download resume", e);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ValidationException("Resume file is empty");
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new ValidationException("Resume file size exceeds 5MB");
        }

        if (!"application/pdf".equals(file.getContentType())) {
            throw new ValidationException("Only PDF resumes are supported");
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "pdf";
        }
        return fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
    }
}