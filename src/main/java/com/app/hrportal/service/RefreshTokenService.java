package com.app.hrportal.service;

import com.app.hrportal.dto.reponse.AuthResponse;

public interface RefreshTokenService {
    String create(String userId);
    AuthResponse refresh(String rawRefreshToken);
    void logout(String rawRefreshToken);
}
