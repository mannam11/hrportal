package com.app.hrportal.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScoreResult {
    private Double requiredSkillsMatchPercentage;
    private Double preferredSkillsMatchPercentage;
    private Double overallFitPercentage;
}
