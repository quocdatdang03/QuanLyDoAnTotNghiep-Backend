package com.quanlydoantotnghiep.DoAnTotNghiep.service.impl;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.DegreeDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.FacultyDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.response.TeacherAccountResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReview.ProgressReviewDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReview.request.CreateProgressReviewRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.*;
import com.quanlydoantotnghiep.DoAnTotNghiep.exception.ApiException;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.*;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.ProgressReviewService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgressReviewServiceImpl implements ProgressReviewService {

    private final ProgressReviewRepository progressReviewRepository;
    private final TeacherRepository teacherRepository;
    private final ProgressReportRepository progressReportRepository;
    private final StageStatusRepository stageStatusRepository;
    private final ProjectStageRepository projectStageRepository;
    private final ProgressReviewFileRepository progressReviewFileRepository;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public ProgressReviewDto createProgressReview(CreateProgressReviewRequest createProgressReviewRequest, AccountDto accountDto) {

        // get teacher for validation:
        Teacher teacher = teacherRepository.findByAccount_AccountId(accountDto.getAccountId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Teacher is not exists with given id: "+accountDto.getAccountId()));

        // get ProgressReport:
        ProgressReport progressReport = progressReportRepository.findById(createProgressReviewRequest.getProgressReportId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "ProgressReport is not exists with given id: "+createProgressReviewRequest.getProgressReportId()));

        // check if teacher does not manage this project -> throw error
        if(!teacher.getTeacherId().equals(progressReport.getProjectStage().getProject().getTeacher().getTeacherId()))
            throw new ApiException(HttpStatus.BAD_REQUEST, "Occur error when create progressReview");

        // check if progressReport is already approved before -> throw error
        if(progressReport.isApproved())
            throw new ApiException(HttpStatus.BAD_REQUEST, "You can not create progressReview on approved progressReport");

        // create progressReview
        ProgressReview progressReview = ProgressReview.builder()
                .progressReviewTitle(createProgressReviewRequest.getProgressReviewTitle())
                .progressReviewContent(createProgressReviewRequest.getProgressReviewContent())
                .progressReport(progressReport)
                .isApproved(createProgressReviewRequest.isApproved())
                .teacher(teacher)
                .flagDelete(false)
                .build();

        List<ProgressReviewFile> progressReviewFiles = createProgressReviewRequest.getProgressReviewFiles()
                .stream().map((item) -> {

                    return ProgressReviewFile.builder()
                            .nameFile(item.getNameFile())
                            .pathFile(item.getPathFile())
                            .progressReview(progressReview)
                            .build();

                }).collect(Collectors.toList());

        progressReview.setProgressReviewFiles(progressReviewFiles);

        ProgressReview savedProgressReview = progressReviewRepository.save(progressReview);

        // check if progressReview is approved -> set isApproved of ProgressReport = true Else = false
        if(savedProgressReview.isApproved())
        {
            progressReport.setApproved(true);
            progressReportRepository.save(progressReport); // save progressReport

            // change status of current stage to 3 ('Đã hoàn thành')
            ProjectStage currentProjectStage = progressReport.getProjectStage();
            StageStatus completedStageStatus = stageStatusRepository.findById(3L)
                    .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "StageStatus is not exists with given id:" +3));
            currentProjectStage.setStageStatus(completedStageStatus);
            projectStageRepository.save(currentProjectStage); // save current ProjectStage


            // get next stage
            ProjectStage nextProjectStage = projectStageRepository.findNextStageOfProject(
                    currentProjectStage.getProject().getProjectId(),
                    currentProjectStage.getStage().getStageOrder()
            ).stream().findFirst().orElse(null);

            if(nextProjectStage!=null){
                // change status of next stage to 2 ('Đang thực hiện')
                StageStatus inProgressStageStatus = stageStatusRepository.findById(2L)
                        .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "StageStatus is not exists with given id:" +2));

                nextProjectStage.setStageStatus(inProgressStageStatus);
                projectStageRepository.save(nextProjectStage); // save next ProjectStage
            }
        }
        else {
            progressReport.setApproved(false);
            progressReportRepository.save(progressReport);
        }

        // convert to ProgressReviewDto
        ProgressReviewDto progressReviewDto = modelMapper.map(progressReview, ProgressReviewDto.class);

        TeacherAccountResponse teacherAccountResponse = modelMapper.map(progressReview.getTeacher().getAccount(), TeacherAccountResponse.class);
        teacherAccountResponse.setTeacherCode(progressReview.getTeacher().getAccount().getCode());
        teacherAccountResponse.setDegree(
                modelMapper.map(progressReview.getTeacher().getDegree(), DegreeDto.class)
        );
        teacherAccountResponse.setFaculty(
                modelMapper.map(progressReview.getTeacher().getFaculty(), FacultyDto.class)
        );
        teacherAccountResponse.setLeader(progressReview.getTeacher().isLeader());

        progressReviewDto.setTeacher(teacherAccountResponse);

        return progressReviewDto;
    }

    @Override
    public String deleteProgressReviewFileById(Long progressReviewFileId, AccountDto accountDto) {

        // get teacher for validation
        Teacher teacher = teacherRepository.findByAccount_AccountId(accountDto.getAccountId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Teacher is not exists with given accountId: "+accountDto.getAccountId()));

        // get progress review file
        ProgressReviewFile progressReviewFile = progressReviewFileRepository.findById(progressReviewFileId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "ProgressReviewFile is not exists with given id: "+progressReviewFileId));

        // check if progressReviewFile does not belong to this teacher -> throw error
        if(!teacher.getTeacherId().equals(progressReviewFile.getProgressReview().getTeacher().getTeacherId()))
            throw new ApiException(HttpStatus.BAD_REQUEST, "Occur error whem delete ProgressReviewFile");

        // delete progressReviewFile
        progressReviewFileRepository.deleteById(progressReviewFile.getProgressReviewFileId());

        return "ProgressReviewFile with id: "+progressReviewFile.getProgressReviewFileId()+" was deleted successfully";
    }

}
