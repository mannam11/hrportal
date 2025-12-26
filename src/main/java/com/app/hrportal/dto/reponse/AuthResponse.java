package com.app.hrportal.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String userId;
    private String fullName;
    private String email;
    private String orgName;
    private LocalDateTime lastUpdatedAt;
    private String accessToken;
}
