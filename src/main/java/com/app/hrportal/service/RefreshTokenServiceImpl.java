package com.app.hrportal.service;

import com.app.hrportal.dto.reponse.AuthResponse;
import com.app.hrportal.exception.InvalidException;
import com.app.hrportal.model.RefreshToken;
import com.app.hrportal.model.User;
import com.app.hrportal.repository.RefreshTokenRepository;
import com.app.hrportal.repository.UserRepository;
import com.fasterxml.uuid.Generators;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService{

    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public String create(String userId) {

        LocalDateTime now = LocalDateTime.now();

        refreshTokenRepository
                .findByUserIdAndRevokedFalseAndExpiresAtAfter(userId, now)
                .ifPresent(token -> {
                    token.setRevoked(true);
                    refreshTokenRepository.save(token);
                });

        String rawToken = Generators.timeBasedEpochGenerator().generate().toString();

        RefreshToken refreshToken = RefreshToken.builder()
                .id(Generators.timeBasedEpochGenerator().generate().toString())
                .revoked(false)
                .tokenHash(passwordEncoder.encode(rawToken))
                .userId(userId)
                .createdAt(now)
                .expiresAt(now.plusDays(7))
                .build();

        refreshTokenRepository.save(refreshToken);

        return rawToken;
    }

    @Override
    public AuthResponse refresh(String rawRefreshToken) {

        RefreshToken refreshToken = checkIfExists();

        if (!passwordEncoder.matches(rawRefreshToken, refreshToken.getTokenHash())) {
            revoke(refreshToken);
            throw new InvalidException("Please login again");
        }

        if (LocalDateTime.now().isAfter(refreshToken.getExpiresAt())) {
            revoke(refreshToken);
            throw new InvalidException("Please login again");
        }

        User user = userRepository.findById(refreshToken.getUserId())
                .orElseThrow(() ->
                        new UsernameNotFoundException("Invalid user")
                );

        return AuthResponse.builder()
                .accessToken(jwtService.generateAccessToken(user))
                .build();
    }

    private RefreshToken checkIfExists() {
        return refreshTokenRepository
                .findByRevokedFalse()
                .orElseThrow(() ->
                        new InvalidException("Please login again")
                );
    }

    @Override
    public void logout(String rawRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository
                .findByRevokedFalse()
                .orElse(null);

        if (refreshToken == null) {
            return;
        }

        if (passwordEncoder.matches(rawRefreshToken, refreshToken.getTokenHash())) {
            revoke(refreshToken);
        }
    }

    private void revoke(RefreshToken refreshToken) {
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }
}
