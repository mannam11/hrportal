package com.app.hrportal.repository;

import com.app.hrportal.model.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RefreshTokenRepository extends MongoRepository<RefreshToken,String> {
    Optional<RefreshToken> findByUserIdAndRevokedFalseAndExpiresAtAfter(
            String userId,
            LocalDateTime now
    );
    Optional<RefreshToken> findByIdAndRevokedFalse(String tokenId);
}
