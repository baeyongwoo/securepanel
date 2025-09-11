package com.innotium.devops.securepanel.service;

import com.innotium.devops.securepanel.dto.DiskInfo;
import com.innotium.devops.securepanel.dto.SystemStatusDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import oshi.SystemInfo;
import oshi.hardware.*;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

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
                return new SystemStatusDto("Unknown", "--", "--", List.of());
            }
        } catch (Exception e) {
            log.error("[StatusService] 시스템 상태 수집 중 오류 발생", e);
            return new SystemStatusDto("오류", "--", "--", List.of());
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

            // 디스크 정보 리스트 구성 (실제 사용량 기준)
            List<DiskInfo> diskList = new ArrayList<>();
            for (HWDiskStore disk : disks) {
                List<HWPartition> partitions = disk.getPartitions();
                for (HWPartition partition : partitions) {
                    String mount = partition.getMountPoint(); // 예: "C:\\"
                    String fsType = partition.getType();      // 예: "NTFS"
                    String name = mount + " (" + fsType + ")";

                    try {
                        File drive = new File(mount);
                        long total = drive.getTotalSpace();      // 전체 용량
                        long free = drive.getFreeSpace();        // 사용 가능 공간
                        long used = total - free;

                        double usedGB = used / (1024.0 * 1024 * 1024);
                        double totalGB = total / (1024.0 * 1024 * 1024);

                        // 유효한 드라이브만 추가
                        if (totalGB > 0) {
                            diskList.add(new DiskInfo(name, usedGB, totalGB));
                        }
                    } catch (Exception e) {
                        log.warn("[Disk] 드라이브 접근 실패: {}", mount);
                    }
                }
            }

            return new SystemStatusDto(osLabel, String.format("%.1f%%", cpuLoad), ram, diskList);

        } catch (Exception e) {
            log.error("[getSystemStatus] 시스템 정보 수집 중 오류 발생", e);
            return new SystemStatusDto(osLabel, "--", "--", List.of());
        }
    }

    private String formatBytes(long bytes) {
        double gb = bytes / (1024.0 * 1024 * 1024);
        return String.format("%.2fGB", gb);
    }
}