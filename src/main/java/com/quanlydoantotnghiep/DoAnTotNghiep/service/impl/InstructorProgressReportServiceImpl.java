package com.quanlydoantotnghiep.DoAnTotNghiep.service.impl;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.DegreeDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.FacultyDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.response.TeacherAccountResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReport.ProgressReportDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReport.ProgressReportFileDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReview.ProgressReviewDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.stage.StageDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.stage.StageStatusDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.ProgressReport;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.Stage;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.StageStatus;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.Teacher;
import com.quanlydoantotnghiep.DoAnTotNghiep.exception.ApiException;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.ProgressReportRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.TeacherRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.InstructorProgressReportService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstructorProgressReportServiceImpl implements InstructorProgressReportService {

    private final ProgressReportRepository progressReportRepository;
    private final TeacherRepository teacherRepository;
    private final ModelMapper modelMapper;

    @Override
    public ProgressReportDto getProgressReportById(Long progressReportId, AccountDto accountDto) {

        // get teacher for validation
        Teacher teacher = teacherRepository.findByAccount_AccountId(accountDto.getAccountId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Teacher is not exists with given id: "+accountDto.getAccountId()));

        ProgressReport progressReport = progressReportRepository.findById(progressReportId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "ProgressReport is not exists with given id: "+progressReportId));

        if(!teacher.getTeacherId().equals(progressReport.getProjectStage().getProject().getTeacher().getTeacherId()))
            throw new ApiException(HttpStatus.BAD_REQUEST, "Occur error when get progressReport by Id");

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
}
