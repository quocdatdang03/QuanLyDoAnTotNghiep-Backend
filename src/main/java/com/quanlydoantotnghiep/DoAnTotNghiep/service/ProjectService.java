package com.quanlydoantotnghiep.DoAnTotNghiep.service;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.project.ProjectDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.project.request.CreateProjectRequest;

public interface ProjectService {

    ProjectDto createProject(CreateProjectRequest createProjectRequest);
    ProjectDto getProjectByStudentCode(String studentCode);
    ProjectDto updateProject(Long projectId, AccountDto accountDto, CreateProjectRequest updateProjectRequest);
    String deleteProjectById(Long projectId);
    String deleteProjectFileByProjectFileId(Long projectFileId);
}
