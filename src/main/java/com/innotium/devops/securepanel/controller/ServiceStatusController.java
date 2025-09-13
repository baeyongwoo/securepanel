package com.innotium.devops.securepanel.controller;

import com.innotium.devops.securepanel.dto.ServiceStatusDto;
import com.innotium.devops.securepanel.service.ServiceStatusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ServiceStatusController {

    private final ServiceStatusService serviceStatusService;

    private static final Set<String> ALLOWED_SERVICES = Set.of("redis", "mariadb", "nginx", "tomcat");
    private static final Set<String> ALLOWED_ACTIONS = Set.of("restart");

    public ServiceStatusController(ServiceStatusService serviceStatusService) {
        this.serviceStatusService = serviceStatusService;
    }

    @GetMapping("/service-status")
    public List<ServiceStatusDto> getServiceStatuses() {
        return serviceStatusService.getAllStatuses();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/service-control")
    public ResponseEntity<Map<String, Object>> controlService(
            @RequestParam String target,
            @RequestParam String action,
            Principal principal
    ) {
        Map<String, Object> result = new HashMap<>();

        String normalizedTarget = target.toLowerCase();
        String normalizedAction = action.toLowerCase();

        if (!ALLOWED_SERVICES.contains(normalizedTarget) || !ALLOWED_ACTIONS.contains(normalizedAction)) {
            result.put("success", false);
            result.put("message", "허용되지 않은 요청입니다.");
            return ResponseEntity.badRequest().body(result);
        }

        // ✅ OS가 Linux가 아니면 실행 생략
        if (!isLinux()) {
            result.put("success", false);
            result.put("message", "현재 OS에서는 서비스 제어가 지원되지 않습니다.");
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(result);
        }

        // ✅ 관리자 권한 체크
        if (!isAdmin(principal)) {
            result.put("success", false);
            result.put("message", "관리자만 실행할 수 있습니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
        }

        String command = "sudo systemctl " + normalizedAction + " " + normalizedTarget;
        boolean success = false;
        String message = "";

        try {
            success = runCommand(command);
            message = success ? "" : "명령어 실행 실패";
        } catch (Exception e) {
            message = "시스템 오류 발생";
        }

        System.out.printf("[제어 로그] 사용자: %s, 서비스: %s, 액션: %s, 성공: %s%n",
                principal != null ? principal.getName() : "anonymous",
                normalizedTarget, normalizedAction, success);

        result.put("success", success);
        result.put("message", message);
        return ResponseEntity.ok(result);
    }

    private boolean runCommand(String command) throws IOException, InterruptedException {
        Process process = new ProcessBuilder("bash", "-c", command).start();
        return process.waitFor() == 0;
    }

    private boolean isAdmin(Principal principal) {
        // 실제 구현에서는 SecurityContext 또는 DB 기반 권한 체크
        return principal != null && principal.getName().equals("admin");
    }

    private boolean isLinux() {
        String osName = System.getProperty("os.name").toLowerCase();
        return osName.contains("nux") || osName.contains("nix");
    }
}