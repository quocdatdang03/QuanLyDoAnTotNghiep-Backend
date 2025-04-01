package com.quanlydoantotnghiep.DoAnTotNghiep.controller;

import com.quanlydoantotnghiep.DoAnTotNghiep.service.ProgressReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/instructor/progressReports")
@RequiredArgsConstructor
public class InstructorProgressReportController {

    private final ProgressReportService progressReportService;

    @GetMapping
    public ResponseEntity<?> getAllProgressReportsByProject(
            @RequestParam("projectId") Long projectId,
            @RequestParam(name = "sortDir", required = false, defaultValue = "desc") String sortDir,
            @RequestParam(name = "stageId", required = false) Long stageId
    ) {

        return ResponseEntity.ok(progressReportService.getAllProgressReportByProject(projectId, stageId, sortDir));
    }
}
