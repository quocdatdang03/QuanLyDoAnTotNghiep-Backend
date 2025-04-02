package com.quanlydoantotnghiep.DoAnTotNghiep.service;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReport.ProgressReportDto;

public interface InstructorProgressReportService {

    ProgressReportDto getProgressReportById(Long progressReportId, AccountDto accountDto);
}
