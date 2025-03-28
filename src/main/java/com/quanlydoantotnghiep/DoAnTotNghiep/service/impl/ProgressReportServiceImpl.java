package com.quanlydoantotnghiep.DoAnTotNghiep.service.impl;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.DegreeDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.FacultyDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.response.TeacherAccountResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReport.ProgressReportDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReport.ProgressReportFileDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReport.request.CreateProgressReportRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.stage.StageDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.stage.StageStatusDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.*;
import com.quanlydoantotnghiep.DoAnTotNghiep.exception.ApiException;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.ProgressReportRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.ProjectStageRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.StudentRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.ProgressReportService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgressReportServiceImpl implements ProgressReportService {

    private final ProgressReportRepository progressReportRepository;
    private final ProjectStageRepository projectStageRepository;
    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;

    @Override
    public ProgressReportDto createProgressReport(CreateProgressReportRequest createProgressReportRequest, AccountDto accountDto) {

        // get student for validation
        Student student = studentRepository.findByAccount_AccountId(accountDto.getAccountId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Student is not exists with given accountId: "+accountDto.getAccountId()));

        // get ProjectStage:
        ProjectStage projectStage = projectStageRepository.findByStageStageIdAndProjectProjectId(createProgressReportRequest.getStageId(), createProgressReportRequest.getProjectId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "ProjectStage is not exists with given stageId:"+createProgressReportRequest.getStageId()+" and projectId:"+createProgressReportRequest.getProjectId()));

        // check if this project does not belong to student -> throw error
        if(!student.getStudentId().equals(projectStage.getProject().getStudentSemester().getStudent().getStudentId()))
            throw new ApiException(HttpStatus.BAD_REQUEST, "Occur error when create progressReport");

        // Check if stageStatus != 2 ('Đang thực hiện') -> throw error
        if(projectStage.getStageStatus().getStageStatusId()!=2)
            throw new ApiException(HttpStatus.BAD_REQUEST, "Can not create progressReport in this stage");

        // create progressReport
        ProgressReport progressReport = ProgressReport.builder()
                .progressReportTitle(createProgressReportRequest.getProgressReportTitle())
                .progressReportContent(createProgressReportRequest.getProgressReportContent())
                .projectStage(projectStage)
                .isApproved(false) // by default is isApproved is false
                .flagDelete(false)
                .build();

        List<ProgressReportFile> progressReportFiles = createProgressReportRequest.getProgressReportFiles().stream()
                .map((item) -> {

                   return ProgressReportFile.builder()
                            .nameFile(item.getNameFile())
                            .pathFile(item.getPathFile())
                            .progressReport(progressReport)
                            .build();
                }).collect(Collectors.toList());

        progressReport.setProgressReportFiles(progressReportFiles);

        ProgressReport savedProgressReport = progressReportRepository.save(progressReport);


        // convert to progressReportDto:
        ProgressReportDto progressReportDto = ProgressReportDto.builder()
                .progressReportId(savedProgressReport.getProgressReportId())
                .progressReportTitle(savedProgressReport.getProgressReportTitle())
                .progressReportContent(savedProgressReport.getProgressReportContent())
                .isApproved(savedProgressReport.isApproved())
                .progressReportFiles(
                        savedProgressReport.getProgressReportFiles().stream()
                            .map(item -> modelMapper.map(item, ProgressReportFileDto.class)).collect(Collectors.toList())
                )
                .createdDate(savedProgressReport.getCreatedAt())
                .build();

        Stage stage = savedProgressReport.getProjectStage().getStage();
        StageStatus stageStatus = savedProgressReport.getProjectStage().getStageStatus();

        StageDto stageDto = modelMapper.map(stage, StageDto.class);

        TeacherAccountResponse teacherAccountResponse = modelMapper.map(stage.getTeacher().getAccount(), TeacherAccountResponse.class);
        teacherAccountResponse.setTeacherCode(stage.getTeacher().getAccount().getCode());
        teacherAccountResponse.setDegree(
                modelMapper.map(stage.getTeacher().getDegree(), DegreeDto.class)
        );
        teacherAccountResponse.setFaculty(
                modelMapper.map(stage.getTeacher().getFaculty(), FacultyDto.class)
        );
        teacherAccountResponse.setLeader(stage.getTeacher().isLeader());

        stageDto.setTeacher(teacherAccountResponse);
        stageDto.setStageStatus(modelMapper.map(stageStatus, StageStatusDto.class));

        progressReportDto.setStage(stageDto);

        return progressReportDto;
    }
}
