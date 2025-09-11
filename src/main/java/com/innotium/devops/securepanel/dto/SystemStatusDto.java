package com.innotium.devops.securepanel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@AllArgsConstructor
public class SystemStatusDto {
    // Getter & Setter
    private String os;
    private String cpu;
    private String ram;
    private List<DiskInfo> disk;

}