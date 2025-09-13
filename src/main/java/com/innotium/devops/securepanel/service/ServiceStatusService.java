package com.innotium.devops.securepanel.service;

import com.innotium.devops.securepanel.dto.ServiceStatusDto;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceStatusService {

    public boolean isRedisRunning() {
        return runCommand("redis-cli ping", "PONG");
    }

    public boolean isMariaDbRunning() {
        return runCommand("mysqladmin ping -u root -p1234", "mysqld is alive");
    }

    public boolean isNginxRunning() {
        return runCommand("systemctl is-active nginx", "active");
    }

    public boolean isTomcatRunning() {
        return runCommand("systemctl is-active tomcat", "active");
    }

    private boolean runCommand(String command, String expectedOutput) {
        try {
            Process process = new ProcessBuilder("bash", "-c", command).start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String output = reader.readLine();
            return output != null && output.trim().contains(expectedOutput);
        } catch (Exception e) {
            return false;
        }
    }

//    public List<ServiceStatusDto> getAllStatuses() {
//        List<ServiceStatusDto> list = new ArrayList<>();
//
//        list.add(new ServiceStatusDto("Redis", isRedisRunning()));
//        list.add(new ServiceStatusDto("MariaDB", isMariaDbRunning()));
//        list.add(new ServiceStatusDto("Nginx", isNginxRunning()));
//        list.add(new ServiceStatusDto("Tomcat", isTomcatRunning()));
//        return list;
//    }
    //윈도우 환경에서의 코드
    public List<ServiceStatusDto> getAllStatuses() {
        List<ServiceStatusDto> list = new ArrayList<>();

        list.add(new ServiceStatusDto("Redis", true));
        list.add(new ServiceStatusDto("MariaDB", true));
        list.add(new ServiceStatusDto("Nginx", false));
        list.add(new ServiceStatusDto("Tomcat", true));
        return list;
    }
}