package com.quanlydoantotnghiep.DoAnTotNghiep.controller;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReport.ProgressReportDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.AccountService;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.InstructorProgressReportService;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.ProgressReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/instructor/progressReports")
@RequiredArgsConstructor
public class InstructorProgressReportController {

    private final ProgressReportService progressReportService;
    private final AccountService accountService;
    private final InstructorProgressReportService instructorProgressReportService;

    @GetMapping
    public ResponseEntity<?> getAllProgressReportsByProject(
            @RequestParam("projectId") Long projectId,
            @RequestParam(name = "sortDir", required = false, defaultValue = "desc") String sortDir,
            @RequestParam(name = "stageId", required = false) Long stageId,
            @RequestParam(name = "status", required = false) Boolean progressReportStatus
    ) {

        return ResponseEntity.ok(progressReportService.getAllProgressReportByProject(projectId, stageId, sortDir, progressReportStatus));
    }

    @GetMapping("/{progressReportId}")
    public ResponseEntity<ProgressReportDto> getProgressReportById(
            @RequestHeader("Authorization") String jwtToken,
            @PathVariable("progressReportId") Long progressReportId
    ) {

        AccountDto accountDto = getAccountDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(instructorProgressReportService.getProgressReportById(progressReportId, accountDto));
    }

    private AccountDto getAccountDtoByJwtToken(String jwtToken) {

        String onlyToken = jwtToken.substring(7);
        AccountDto accountDto = accountService.getByJwtToken(onlyToken);

        return accountDto;
    }
}
