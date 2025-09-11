package com.innotium.devops.securepanel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DiskInfo {
    private String name;
    private double usedGB;
    private double totalGB;

    // getters, setters, constructors
}
