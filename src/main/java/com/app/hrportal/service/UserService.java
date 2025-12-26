package com.app.hrportal.service;

import com.app.hrportal.dto.reponse.AuthResponse;
import com.app.hrportal.dto.request.LoginRequest;
import com.app.hrportal.dto.request.SignupRequest;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    void signup(SignupRequest request);
    AuthResponse login(LoginRequest request);
    UserDetails loadUserById(String userId);
}
