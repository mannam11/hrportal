package com.app.hrportal.controller;

import com.app.hrportal.dto.reponse.AuthResponse;
import com.app.hrportal.dto.request.LoginRequest;
import com.app.hrportal.dto.request.OtpRequest;
import com.app.hrportal.dto.request.SignupRequest;
import com.app.hrportal.dto.request.VerifyOtpRequest;
import com.app.hrportal.exception.InvalidException;
import com.app.hrportal.service.OtpService;
import com.app.hrportal.service.RefreshTokenService;
import com.app.hrportal.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final OtpService otpService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest request){
        userService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Signup successful");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request,
                                              HttpServletResponse response){

        AuthResponse authResponse = userService.login(request);

        String refreshToken = refreshTokenService.create(authResponse.getUserId());

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofDays(7))
                .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                refreshCookie.toString()
        );

        return ResponseEntity.ok(authResponse);
    }


    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response) {

        if (refreshToken == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        try {
            AuthResponse authResponse =
                    refreshTokenService.refresh(refreshToken);

            return ResponseEntity.ok(authResponse);

        } catch (InvalidException ex) {

            // Refresh token invalid → clear cookie
            clearRefreshTokenCookie(response);

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }
    }

    private void clearRefreshTokenCookie(HttpServletResponse response) {

        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) {

        if (refreshToken != null) {
            refreshTokenService.logout(refreshToken);
        }

        clearRefreshTokenCookie(response);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify-email")
    public ResponseEntity<Void> verifyEmail(@RequestBody VerifyOtpRequest request) {
        otpService.verifySignupOtp(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/request-otp")
    public ResponseEntity<Void> requestOtp(
            @Valid @RequestBody OtpRequest request) {

        otpService.requestOtp(request);

        return ResponseEntity.ok().build();
    }

}
