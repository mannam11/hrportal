package com.app.hrportal.service;

import com.app.hrportal.dto.reponse.AuthResponse;
import com.app.hrportal.dto.request.LoginRequest;
import com.app.hrportal.dto.request.SignupEvent;
import com.app.hrportal.dto.request.SignupRequest;
import com.app.hrportal.enums.Role;
import com.app.hrportal.exception.user.EmailAlreadyExistsException;
import com.app.hrportal.model.User;
import com.app.hrportal.repository.UserRepository;
import com.app.hrportal.utils.OtpGenerator;
import com.fasterxml.uuid.Generators;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.random.RandomGenerator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmailService emailService;

    @Override
    public void signup(SignupRequest request) {

        if(userRepository.existsByEmail(request.getEmail())){
            throw new EmailAlreadyExistsException("Email already exists");
        }

        LocalDateTime now  = LocalDateTime.now();

        User user = User.builder()
                .id(Generators.timeBasedEpochGenerator().generate().toString())
                .fullName(request.getFullName())
                .orgName(request.getOrgName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.RECRUITER)
                .createdAt(now)
                .updatedAt(now)
                .build();

        User savedUser = userRepository.save(user);

        log.info("User with email : {} signed up successfully", savedUser.getEmail());

        emailService.send(SignupEvent.builder()
                        .email(savedUser.getEmail())
                        .otp(OtpGenerator.generate())
                .build());
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );

        log.info("User with email : {} authenticated successfully.",request.getEmail());
        User user = (User) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(user);

        return AuthResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .orgName(user.getOrgName())
                .lastUpdatedAt(user.getUpdatedAt())
                .accessToken(accessToken)
                .build();
    }

    @Override
    public UserDetails loadUserById(String userId) {

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found with id: " + userId
                        )
                );
    }
}
