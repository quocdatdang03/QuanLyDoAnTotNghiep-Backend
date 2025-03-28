package com.quanlydoantotnghiep.DoAnTotNghiep.controller;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReport.ProgressReportDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReport.request.CreateProgressReportRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.AccountService;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.ProgressReportService;
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
    private final AccountService accountService;

    @GetMapping("/stages")
    public ResponseEntity<?> getAllStagesByProject(
            @RequestHeader("Authorization") String jwtToken,
            @RequestParam("projectId") Long projectId
    ) {

        AccountDto accountDto = getAccountDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(studentStageService.getAllStagesByProject(projectId, accountDto));
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

    private AccountDto getAccountDtoByJwtToken(String jwtToken) {

        String onlyToken = jwtToken.substring(7);
        AccountDto accountDto = accountService.getByJwtToken(onlyToken);

        return accountDto;
    }
}
