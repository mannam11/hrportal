package com.app.hrportal.service;

import com.app.hrportal.dto.reponse.AnalysisResponse;
import com.app.hrportal.model.Application;
import com.app.hrportal.model.JobPost;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
public class OpenAIResumeAnalysisService implements ResumeAnalysisService{

    private final ChatClient chatClient;

    @Autowired
    private DocumentReaderService documentReaderService;

    public OpenAIResumeAnalysisService(ChatClient.Builder builder){
        this.chatClient = builder.build();
    }

    @Override
    public AnalysisResponse analyse(InputStream inputStream, JobPost jobPost, Application application) {
        String resumeContent = documentReaderService.readText(inputStream, application.getResumeKey());

        if(resumeContent.isBlank()){
            log.info("No resume content found for application with id : {} ",application.getId());
            return null;
        }

        log.info("AI analysis is getting started for application with id : {}",application.getId());

        return chatClient.prompt()
                .system(systemPrompt())
                .user(userPrompt(resumeContent,
                        jobPost.getDescription(),
                        jobPost.getRequiredSkills(),
                        jobPost.getPreferredSkills()))
                .call()
                .entity(AnalysisResponse.class);

    }


    private String systemPrompt() {

        String prompt = "You are an advanced Resume Analysis Engine used in a professional HR evaluation system.\n" +
                "Your role is to analyze a candidate’s resume with high accuracy and compare it against:\n" +
                "\n" +
                "• The job description\n" +
                "• The required skills\n" +
                "• The preferred skills\n" +
                "\n" +
                "Your evaluation must be precise, factual, and strictly based on the content found in the resume text provided.\n" +
                "\n" +
                "Key rules for your analysis:\n" +
                "\n" +
                "1. Identify which required skills are explicitly demonstrated in the resume.\n" +
                "2. Identify which required skills are missing or not clearly supported by the resume.\n" +
                "3. Identify which preferred skills are present and which are not.\n" +
                "4. Do not assume skills; only count a skill as present if the resume text clearly supports it.\n" +
                "5. Evaluate the candidate’s alignment with the role based on technical and contextual information found in the resume.\n" +
                "6. Provide an accurate assessment of strengths, gaps, and suitability for the job.\n" +
                "7. Maintain a professional, objective, and consistent evaluation tone.\n" +
                "8. Base all conclusions strictly on the resume text—do not infer skills, experience, or proficiency beyond what is written.\n" +
                "\n" +
                "The goal is to deliver a clear and reliable comparison between the resume and the job’s expectations.\n";

        return prompt;
    }

    private String userPrompt(String resumeContent,
                              String jobDescription,
                              List<String> requiredSkills,
                              List<String> preferredSkills) {
        String prompt = """
                Analyze the candidate’s resume against the provided job information.
                
                RESUME CONTENT:
                %s
                
                JOB DESCRIPTION:
                %s
                
                REQUIRED SKILLS:
                %s
                
                PREFERRED SKILLS:
                %s
                
                Using the above information, evaluate how well the candidate’s resume aligns with the job expectations,
                focusing on required and preferred skills. Base your analysis strictly on the content found in the resume
                without making assumptions.
                """.formatted(resumeContent, jobDescription, requiredSkills, preferredSkills);

        return prompt;
    }

}
