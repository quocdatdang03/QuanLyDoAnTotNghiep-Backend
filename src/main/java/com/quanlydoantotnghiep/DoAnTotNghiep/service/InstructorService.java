package com.quanlydoantotnghiep.DoAnTotNghiep.service;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.ObjectResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.project.ProjectDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.stage.StageDto;

import java.util.List;

public interface InstructorService {

    ProjectDto getProjectByStudentCodeAndSemester(String studentCode, Long semesterId);
    ProjectDto getProjectByProjectId(Long projectId);
    ObjectResponse getAllStudentsOfInstructor(AccountDto accountDto, String keyword, Long semesterId, Long classId, Boolean havingProject, int pageNumber, int pageSize, String sortBy, String sortDir);
    ObjectResponse getAllProjectsManagedByInstructor(AccountDto accountDto, String keyword, Long semesterId, Long classId, int pageNumber, int pageSize, String sortBy, String sortDir);
    ProjectDto approveProject(Long projectId);
    ProjectDto declineProject(Long projectId);

    // methods for progress manager:
    List<StageDto> getAllStagesByProject(Long projectId);
}
