package com.quanlydoantotnghiep.DoAnTotNghiep.service;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.stage.StageDto;

import java.util.List;

public interface StudentStageService {

    List<StageDto> getAllStagesByProject(Long projectId, AccountDto accountDto);
}
