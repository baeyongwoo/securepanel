package com.innotium.devops.securepanel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ServiceStatusDto {
    private String name;
    private boolean running;

    public ServiceStatusDto(String name, boolean running) {
        this.name = name;
        this.running = running;
    }

    // getters & setters
}