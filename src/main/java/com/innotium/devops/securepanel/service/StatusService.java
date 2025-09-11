package com.innotium.devops.securepanel.service;

import com.innotium.devops.securepanel.dto.SystemStatusDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.*;

import java.util.List;

@Service
public class StatusService {

    private static final Logger log = LoggerFactory.getLogger(StatusService.class);
    private long[] previousTicks = null;

    public SystemStatusDto getStatus() {
        try {
            String osName = System.getProperty("os.name").toLowerCase();

            if (osName.contains("win")) {
                return getSystemStatus("Windows");
            } else if (osName.contains("nux") || osName.contains("nix")) {
                return getSystemStatus("Linux");
            } else {
                return new SystemStatusDto("Unknown", "--", "--", "--");
            }
        } catch (Exception e) {
            log.error("[StatusService] 시스템 상태 수집 중 오류 발생", e);
            return new SystemStatusDto("오류", "--", "--", "--");
        }
    }

    private SystemStatusDto getSystemStatus(String osLabel) {
        try {
            SystemInfo systemInfo = new SystemInfo();
            HardwareAbstractionLayer hal = systemInfo.getHardware();

            CentralProcessor cpu = hal.getProcessor();
            GlobalMemory memory = hal.getMemory();
            List<HWDiskStore> disks = hal.getDiskStores();

            // CPU 사용률 계산
            double cpuLoad = 0.0;
            if (previousTicks == null) {
                previousTicks = cpu.getSystemCpuLoadTicks();
            } else {
                cpuLoad = cpu.getSystemCpuLoadBetweenTicks(previousTicks) * 100;
                previousTicks = cpu.getSystemCpuLoadTicks();
            }

            // RAM 계산
            long usedMemory = memory.getTotal() - memory.getAvailable();
            long totalMemory = memory.getTotal();
            String ram = formatBytes(usedMemory) + " / " + formatBytes(totalMemory);

            // 디스크 정보
            StringBuilder diskInfo = new StringBuilder();
            for (HWDiskStore disk : disks) {
                String name = disk.getName();
                String read = formatBytes(disk.getReadBytes());
                String write = formatBytes(disk.getWriteBytes());

                diskInfo.append(name)
                        .append(": 읽기 ")
                        .append(read)
                        .append(", 쓰기 ")
                        .append(write)
                        .append("\n");
            }

            return new SystemStatusDto(osLabel, String.format("%.1f%%", cpuLoad), ram, diskInfo.toString().trim());

        } catch (Exception e) {
            log.error("[getSystemStatus] 시스템 정보 수집 중 오류 발생", e);
            return new SystemStatusDto(osLabel, "--", "--", "--");
        }
    }

    private String formatBytes(long bytes) {
        double gb = bytes / (1024.0 * 1024 * 1024);
        return String.format("%.2fGB", gb);
    }
}