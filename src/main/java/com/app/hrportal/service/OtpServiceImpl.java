package com.app.hrportal.service;

import com.app.hrportal.dto.request.DomainEvent;
import com.app.hrportal.dto.request.OtpRequest;
import com.app.hrportal.dto.request.VerifyOtpRequest;
import com.app.hrportal.enums.OtpPurpose;
import com.app.hrportal.exception.InvalidException;
import com.app.hrportal.model.User;
import com.app.hrportal.repository.UserRepository;
import com.app.hrportal.utils.OtpGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpServiceImpl implements OtpService {

    private final UserRepository userRepository;
    private final EventPublisher eventPublisher;
    private final OtpEventFactory otpEventFactory;

    @Override
    @Transactional
    public void verifySignupOtp(VerifyOtpRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidException("Invalid email or OTP"));

        User.Otp otp = user.getOtp();

        if (otp == null) {
            throw new InvalidException("No OTP found");
        }

        if (otp.isUsed()) {
            throw new InvalidException("OTP already used");
        }

        if (otp.getPurpose() != OtpPurpose.SIGNUP) {
            throw new InvalidException("Invalid OTP purpose");
        }

        if (!otp.getCode().equals(request.getOtp())) {
            throw new InvalidException("Invalid email or OTP");
        }

        if (LocalDateTime.now().isAfter(otp.getExpiresAt())) {
            throw new InvalidException("OTP expired");
        }

        // ✅ Verification success
        otp.setUsed(true);
        user.setEmailVerified(true);
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        log.info("Email verified successfully for {}", user.getEmail());
    }

    @Override
    @Transactional
    public void requestOtp(OtpRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidException("User not found"));

        OtpPurpose purpose = request.getPurpose();

        // Guard: signup OTP requested but already verified
        if (purpose == OtpPurpose.SIGNUP && user.isEmailVerified()) {
            throw new InvalidException("Email already verified");
        }

        // Optional: simple resend throttling (prevent spam)
        User.Otp existingOtp = user.getOtp();
        if (existingOtp != null &&
                existingOtp.getCreatedAt().isAfter(LocalDateTime.now().minusMinutes(1))) {
            throw new InvalidException("Please wait before requesting another OTP");
        }

        LocalDateTime now = LocalDateTime.now();

        User.Otp newOtp = User.Otp.builder()
                .code(OtpGenerator.generate())
                .purpose(purpose)
                .used(false)
                .createdAt(now)
                .expiresAt(now.plusMinutes(10))
                .build();

        // Replace old OTP (expired / used / wrong purpose – doesn’t matter)
        user.setOtp(newOtp);
        user.setUpdatedAt(now);

        userRepository.save(user);

        Object event = otpEventFactory.createEvent(
                user.getEmail(),
                newOtp.getCode(),
                request.getPurpose()
        );

        eventPublisher.publish((DomainEvent) event);


        log.info("OTP generated for email={} purpose={}", user.getEmail(), purpose);
    }
}

