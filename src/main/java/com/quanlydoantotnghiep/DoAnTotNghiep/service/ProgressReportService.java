package com.quanlydoantotnghiep.DoAnTotNghiep.service;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReport.ProgressReportDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReport.request.CreateProgressReportRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReport.request.UpdateProgressReportRequest;

import java.util.List;

public interface ProgressReportService {

    List<ProgressReportDto> getAllProgressReportByProject(Long projectId, Long stageId, String sortDir, Boolean progressReportStatus);
    ProgressReportDto getProgressReportById(Long progressReportId);
    ProgressReportDto createProgressReport(CreateProgressReportRequest createProgressReportRequest, AccountDto accountDto);
    ProgressReportDto updateProgressReport(UpdateProgressReportRequest updateProgressReportRequest, AccountDto accountDto);
    String deleteProgressReportById(Long progressReportId, AccountDto accountDto);
    String deleteProgressReportFileById(Long progressReportFileId);
}
