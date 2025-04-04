package com.quanlydoantotnghiep.DoAnTotNghiep.service.impl;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.DegreeDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.FacultyDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.response.TeacherAccountResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.stage.StageDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.stage.StageFileDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.stage.StageStatusDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.*;
import com.quanlydoantotnghiep.DoAnTotNghiep.exception.ApiException;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.ProjectRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.ProjectStageRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.StageRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.StudentRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.StudentStageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentStageServiceImpl implements StudentStageService {

    private final ProjectStageRepository projectStageRepository;
    private final ProjectRepository projectRepository;
    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<StageDto> getAllStagesByProject(Long projectId, AccountDto accountDto) {

        // get student for validation:
        Student student = studentRepository.findByAccount_AccountId(accountDto.getAccountId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Student is not exists with given id: "+accountDto.getAccountId()));

        // get Project for validation:
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Project is not exists with given id: "+projectId));

        // check if this project does not belong to this student -> throw error
        if(!project.getStudentSemester().getStudent().getStudentId().equals(student.getStudentId()))
            throw new ApiException(HttpStatus.BAD_REQUEST, "Occur error when get all stages by project");

        // sort by stageOrder by ascending:
        Sort sort = Sort.by("stage.stageOrder").ascending();

        // get List ProjectStage by projectId
        List<ProjectStage> projectStages = projectStageRepository.findAllByProjectProjectId(projectId, sort);

        // convert to list StageDto:
        List<StageDto> stageDtos = projectStages.stream().map(
                (projectStage) -> {

                    Stage stage = projectStage.getStage();
                    StageStatus stageStatus = projectStage.getStageStatus();

                    // convert to StageDto:
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

                    return stageDto;
                }
        ).collect(Collectors.toList());


        return stageDtos;
    }

    @Override
    public StageDto getCurrentStageByProject(Long projectId, AccountDto accountDto) {

        // get student for validation:
        Student student = studentRepository.findByAccount_AccountId(accountDto.getAccountId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Student is not exists with given id: "+accountDto.getAccountId()));

        // get Project for validation:
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Project is not exists with given id: "+projectId));

        // check if this project does not belong to this student -> throw error
        if(!project.getStudentSemester().getStudent().getStudentId().equals(student.getStudentId()))
            throw new ApiException(HttpStatus.BAD_REQUEST, "Occur error when get current stage by project");

        // get ProjectStage having stageStatus = 2 ('Đang thực hiện')
        ProjectStage projectStage = projectStageRepository.findInProgressProjectStageByProjectId(projectId);

        // if ProjectStage having stageStatus = 2 is null -> get latest completed ProjectStage (having stageStatus = 3 ('Đang hoàn thành'))
        if(projectStage==null){
            projectStage = projectStageRepository.findLatestCompletedProjectStageByProjectId(projectId)
                    .stream().findFirst().orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Current stage is unavailable"));
        }

        Stage currentStage = projectStage.getStage();

        // convert to StageDto:
        StageDto stageDto = modelMapper.map(currentStage, StageDto.class);

        TeacherAccountResponse teacherAccountResponse = modelMapper.map(currentStage.getTeacher().getAccount(), TeacherAccountResponse.class);
        teacherAccountResponse.setTeacherCode(currentStage.getTeacher().getAccount().getCode());
        teacherAccountResponse.setDegree(
                modelMapper.map(currentStage.getTeacher().getDegree(), DegreeDto.class)
        );
        teacherAccountResponse.setFaculty(
                modelMapper.map(currentStage.getTeacher().getFaculty(), FacultyDto.class)
        );
        teacherAccountResponse.setLeader(currentStage.getTeacher().isLeader());

        stageDto.setTeacher(teacherAccountResponse);
        stageDto.setStageStatus(modelMapper.map(projectStage.getStageStatus(), StageStatusDto.class));

        return stageDto;
    }
}
