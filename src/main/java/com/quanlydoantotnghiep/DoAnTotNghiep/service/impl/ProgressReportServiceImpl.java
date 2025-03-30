package com.quanlydoantotnghiep.DoAnTotNghiep.service.impl;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.DegreeDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.FacultyDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.response.TeacherAccountResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReport.ProgressReportDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReport.ProgressReportFileDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReport.request.CreateProgressReportRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReport.request.UpdateProgressReportRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReview.ProgressReviewDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.stage.StageDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.stage.StageStatusDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.*;
import com.quanlydoantotnghiep.DoAnTotNghiep.exception.ApiException;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.ProgressReportFileRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.ProgressReportRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.ProjectStageRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.StudentRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.ProgressReportService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
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
    private final ProgressReportFileRepository progressReportFileRepository;
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

    @Override
    public ProgressReportDto updateProgressReport(UpdateProgressReportRequest updateProgressReport, AccountDto accountDto) {

        // get student for validation
        Student student = studentRepository.findByAccount_AccountId(accountDto.getAccountId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Student is not exists with given accountId: "+accountDto.getAccountId()));

        // get progressReport
        ProgressReport progressReport = progressReportRepository.findById(updateProgressReport.getProgressReportId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "ProgressReport is not exists with given id: "+updateProgressReport.getProgressReportId()));

        // check if this progressReport does not belong to student -> throw error
        if(!student.getStudentId().equals(progressReport.getProjectStage().getProject().getStudentSemester().getStudent().getStudentId()))
            throw new ApiException(HttpStatus.BAD_REQUEST, "Occur error when update progressReport");

        // update progressReport:
        progressReport.setProgressReportTitle(updateProgressReport.getProgressReportTitle());
        progressReport.setProgressReportContent(updateProgressReport.getProgressReportContent());

        // Delete all old progressReportFiles before add new
        progressReport.getProgressReportFiles().clear();

        // add new list projectFiles
        List<ProgressReportFile> newProgressReportFiles = updateProgressReport.getProgressReportFiles().stream().map(
                (item) -> {
                    return ProgressReportFile.builder()
                            .nameFile(item.getNameFile())
                            .pathFile(item.getPathFile())
                            .progressReport(progressReport)
                            .build();
                }
        ).collect(Collectors.toList());

        progressReport.getProgressReportFiles().addAll(newProgressReportFiles);

        ProgressReport savedProgressReport = progressReportRepository.save(progressReport);

        return convertToProgressReportDto(savedProgressReport);
    }

    @Override
    public String deleteProgressReportById(Long progressReportId, AccountDto accountDto) {

        // get progressReport for validation
        ProgressReport progressReport = progressReportRepository.findById(progressReportId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "ProgressReport is not exists with given id: "+progressReportId));

        // get student for validation
        Student student = studentRepository.findByAccount_AccountId(accountDto.getAccountId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Student is not exists with given accountId: "+accountDto.getAccountId()));

        // check if this progressReport does not belong to student -> throw error
        if(!student.getStudentId().equals(progressReport.getProjectStage().getProject().getStudentSemester().getStudent().getStudentId()))
            throw new ApiException(HttpStatus.BAD_REQUEST, "Occur error when delete progressReport");

        // check if progressReport is approved or had progressReviews -> don't allow delete
        if(progressReport.isApproved() || progressReport.getProgressReviews().size() > 0)
            throw new ApiException(HttpStatus.BAD_REQUEST, "Can not delete this ProgressReport");

        progressReportRepository.deleteById(progressReportId);

        return "ProgressReport with id: "+progressReportId+" was deleted successfully";
    }

    @Override
    public List<ProgressReportDto> getAllProgressReportByProject(Long projectId) {

        // Sort by createdAt by descending (Lấy cái mới nhất trước)
        Sort sort = Sort.by("createdAt").descending();

        List<ProgressReport> progressReports = progressReportRepository.findProgressReportByProject(projectId, sort);

        return progressReports.stream().map(
                item -> {
                    // convert to progressReportDto:
                    return convertToProgressReportDto(item);
                }
        ).collect(Collectors.toList());
    }

    @Override
    public ProgressReportDto getProgressReportById(Long progressReportId) {

        ProgressReport progressReport = progressReportRepository.findById(progressReportId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "ProgressReport is not exists with given id: "+progressReportId));

        // convert to progressReportDto:
        return convertToProgressReportDto(progressReport);
    }

    private ProgressReportDto convertToProgressReportDto(ProgressReport progressReport) {
        ProgressReportDto progressReportDto = ProgressReportDto.builder()
                .progressReportId(progressReport.getProgressReportId())
                .progressReportTitle(progressReport.getProgressReportTitle())
                .progressReportContent(progressReport.getProgressReportContent())
                .isApproved(progressReport.isApproved())
                .progressReportFiles(
                        progressReport.getProgressReportFiles().stream()
                                .map(progressReportFile -> modelMapper.map(progressReportFile, ProgressReportFileDto.class)).collect(Collectors.toList())
                )
                .createdDate(progressReport.getCreatedAt())
                .build();

        // convert and set stage dto
        Stage stage = progressReport.getProjectStage().getStage();
        StageStatus stageStatus = progressReport.getProjectStage().getStageStatus();

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

        // convert and set progressReviews dto
        List<ProgressReviewDto> progressReviewDtos = progressReport.getProgressReviews().stream()
                .map((progressReview -> {

                    ProgressReviewDto progressReviewDto = modelMapper.map(progressReview, ProgressReviewDto.class);

                    progressReviewDto.setTeacher(teacherAccountResponse);

                    return progressReviewDto;
                })).sorted((o1, o2) -> o2.getCreatedDate().compareTo(o1.getCreatedDate())).collect(Collectors.toList());

        progressReportDto.setProgressReviews(progressReviewDtos);

        return progressReportDto;
    }

    @Override
    public String deleteProgressReportFileById(Long progressReportFileId) {

        ProgressReportFile progressReportFile = progressReportFileRepository.findById(progressReportFileId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "ProgressReportFile is not exists with given id: "+progressReportFileId));

        if(progressReportFile.getProgressReport().isApproved() || progressReportFile.getProgressReport().getProgressReviews().size() > 0)
            throw new ApiException(HttpStatus.BAD_REQUEST, "Can not delete this ProgressReportFile");

        progressReportFileRepository.deleteById(progressReportFile.getProgressReportFileId());

        return "ProgressReportFile with id: "+progressReportFileId+" was deleted successfully";
    }
}
