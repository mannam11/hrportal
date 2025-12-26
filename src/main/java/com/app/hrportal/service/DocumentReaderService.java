package com.app.hrportal.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
public class DocumentReaderService {

    public String readText(InputStream inputStream, String fileName) {
        try {
            Resource resource = new InputStreamResource(inputStream) {
                @Override
                public String getFilename() {
                    return fileName;
                }
            };

            TikaDocumentReader reader = new TikaDocumentReader(resource);
            List<Document> documents = reader.get();

            StringBuilder text = new StringBuilder();
            for (Document doc : documents) {
                text.append(doc.getText()).append("\n");
            }

            return text.toString().trim();

        } catch (Exception e) {
            log.error("Error reading resume text for file {}", fileName, e);
            return "";
        }
    }
}