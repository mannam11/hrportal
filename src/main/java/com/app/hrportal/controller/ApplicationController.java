package com.app.hrportal.controller;

import com.app.hrportal.dto.reponse.ApplicationResponse;
import com.app.hrportal.model.User;
import com.app.hrportal.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    @GetMapping("/recent")
    public ResponseEntity<List<ApplicationResponse>> recentAfter(
            @AuthenticationPrincipal User user,
            @RequestParam("after") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime after) {
        return ResponseEntity.ok(
                applicationService.getNewApplicationsAfter(
                        user.getId(),
                        after
                )
        );
    }

}
