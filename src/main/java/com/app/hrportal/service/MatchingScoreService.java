package com.app.hrportal.service;

import com.app.hrportal.dto.reponse.ScoreResult;
import com.app.hrportal.model.Application;
import com.app.hrportal.model.JobPost;
import org.springframework.stereotype.Service;

@Service
public class MatchingScoreService {

    public ScoreResult calculate(
            Application.AnalysisResult analysisResult,
            JobPost jobPost) {

        double requiredPct = calculateRequired(analysisResult);
        double preferredPct = calculatePreferred(analysisResult);
        double overall = calculateOverall(requiredPct, preferredPct, jobPost);

        return ScoreResult.builder()
                .requiredSkillsMatchPercentage(requiredPct)
                .preferredSkillsMatchPercentage(preferredPct)
                .overallFitPercentage(overall)
                .build();
    }

    private double calculateRequired(Application.AnalysisResult r) {
        int total = r.getMatchedRequiredSkills().size()
                + r.getMissingRequiredSkills().size();
        return total == 0 ? 0 : (r.getMatchedRequiredSkills().size() * 100.0) / total;
    }

    private double calculatePreferred(Application.AnalysisResult r) {
        int total = r.getMatchedPreferredSkills().size()
                + r.getMissingPreferredSkills().size();
        return total == 0 ? 0 : (r.getMatchedPreferredSkills().size() * 100.0) / total;
    }

    private double calculateOverall(
            double requiredPct,
            double preferredPct,
            JobPost jobPost) {

        if (jobPost.getPreferredSkills().isEmpty()) {
            return requiredPct;
        }
        return (requiredPct * 0.8) + (preferredPct * 0.2);
    }
}

