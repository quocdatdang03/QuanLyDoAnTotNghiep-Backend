package com.quanlydoantotnghiep.DoAnTotNghiep.controller;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReport.ProgressReportDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReport.request.CreateProgressReportRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReport.request.UpdateProgressReportRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.stage.StageDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.AccountService;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.ProgressReportService;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.StageService;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.StudentStageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student/progressReports")
@RequiredArgsConstructor
public class StudentProgressReportController {

    private final StudentStageService studentStageService;
    private final ProgressReportService progressReportService;
    private final StageService stageService;
    private final AccountService accountService;

    @GetMapping("/stages")
    public ResponseEntity<?> getAllStagesByProject(
            @RequestHeader("Authorization") String jwtToken,
            @RequestParam("projectId") Long projectId
    ) {

        AccountDto accountDto = getAccountDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(studentStageService.getAllStagesByProject(projectId, accountDto));
    }

    @GetMapping("/stages/{stageId}")
    public ResponseEntity<StageDto>  getStageById(
            @PathVariable("stageId") Long stageId
    ) {

        return ResponseEntity.ok(stageService.getStageById(stageId));
    }

    @GetMapping("/project/{projectId}/currentStage")
    public ResponseEntity<StageDto>  getCurrentStageByProject(
            @RequestHeader("Authorization") String jwtToken,
            @PathVariable("projectId") Long projectId
    ) {

        AccountDto accountDto = getAccountDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(studentStageService.getCurrentStageByProject(projectId, accountDto));
    }

    @GetMapping
    public ResponseEntity<?> getAllProgressReportsByProject(
            @RequestParam("projectId") Long projectId,
            @RequestParam(name = "sortDir", required = false, defaultValue = "desc") String sortDir,
            @RequestParam(name = "stageId", required = false) Long stageId
    ) {

        return ResponseEntity.ok(progressReportService.getAllProgressReportByProject(projectId, stageId, sortDir));
    }

    @GetMapping("/{progressReportId}")
    public ResponseEntity<?> getProgressReportById(
            @PathVariable("progressReportId") Long progressReportId
    ) {

        return ResponseEntity.ok(progressReportService.getProgressReportById(progressReportId));
    }

    @PostMapping("/creation")
    public ResponseEntity<ProgressReportDto> createProgressReport(
            @RequestHeader("Authorization") String jwtToken,
            @RequestBody CreateProgressReportRequest createProgressReportRequest
    ) {

        AccountDto accountDto = getAccountDtoByJwtToken(jwtToken);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(progressReportService.createProgressReport(createProgressReportRequest, accountDto));
    }

    @PutMapping
    public ResponseEntity<ProgressReportDto> updateProgressReport(
            @RequestHeader("Authorization") String jwtToken,
            @RequestBody UpdateProgressReportRequest updateProgressReportRequest
    ) {

        AccountDto accountDto = getAccountDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(progressReportService.updateProgressReport(updateProgressReportRequest, accountDto));
    }

    @DeleteMapping("/{progressReportId}")
    public ResponseEntity<String> deleteProgressReportById(
            @RequestHeader("Authorization") String jwtToken,
            @PathVariable("progressReportId") Long progressReportId
    ) {

        AccountDto accountDto = getAccountDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(progressReportService.deleteProgressReportById(progressReportId, accountDto));
    }

    @DeleteMapping("/progressReportFile/{progressReportFileId}")
    public ResponseEntity<String> deleteProgressReportFileById(
            @PathVariable("progressReportFileId") Long progressReportFileId
    ) {

        return ResponseEntity.ok(progressReportService.deleteProgressReportFileById(progressReportFileId));
    }

    private AccountDto getAccountDtoByJwtToken(String jwtToken) {

        String onlyToken = jwtToken.substring(7);
        AccountDto accountDto = accountService.getByJwtToken(onlyToken);

        return accountDto;
    }
}
