package com.innotium.devops.securepanel.controller;

import com.innotium.devops.securepanel.dto.SystemStatusDto;
import com.innotium.devops.securepanel.service.StatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class StatusController {

    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    @GetMapping("/status")
    public ResponseEntity<SystemStatusDto> getSystemStatus() {
        SystemStatusDto status = statusService.getStatus();
        return ResponseEntity.ok(status);
    }
}
