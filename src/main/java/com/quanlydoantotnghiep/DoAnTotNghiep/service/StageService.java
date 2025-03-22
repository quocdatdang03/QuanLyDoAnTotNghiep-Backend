package com.quanlydoantotnghiep.DoAnTotNghiep.service;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.stage.StageDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.stage.StageStatusDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.stage.request.CreateStageRequest;

import java.util.List;

public interface StageService {

    StageDto createStage(AccountDto accountDto, CreateStageRequest createStageRequest);
    StageDto updateStage(Long stageId, AccountDto accountDto, CreateStageRequest updateStageRequest);
    String deleteStage(Long stageId, AccountDto accountDto);
    List<StageDto> getAllStagesByTeacherAndSemester(AccountDto accountDto, Long semesterId);
    StageDto getStageById(Long stageId);
    String deleteStageFileById(Long stageFileId);
    StageDto updateStageStatus(Long stageId, Long stageStatusId);
    List<StageStatusDto> getAllStageStatuses();
}
