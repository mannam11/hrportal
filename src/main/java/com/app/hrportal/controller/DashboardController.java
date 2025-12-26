package com.app.hrportal.controller;

import com.app.hrportal.dto.reponse.DashboardOverviewResponse;
import com.app.hrportal.model.User;
import com.app.hrportal.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/overview")
    public ResponseEntity<DashboardOverviewResponse> getDashboardOverview(@AuthenticationPrincipal User currentUser){
        return ResponseEntity.ok(dashboardService.getDashboardOverview(currentUser.getId()));
    }

}
