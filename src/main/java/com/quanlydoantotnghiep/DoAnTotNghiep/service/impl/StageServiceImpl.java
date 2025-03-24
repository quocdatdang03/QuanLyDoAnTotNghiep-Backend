//package com.quanlydoantotnghiep.DoAnTotNghiep.service.impl;
//
//import com.quanlydoantotnghiep.DoAnTotNghiep.dto.DegreeDto;
//import com.quanlydoantotnghiep.DoAnTotNghiep.dto.FacultyDto;
//import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
//import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.response.TeacherAccountResponse;
//import com.quanlydoantotnghiep.DoAnTotNghiep.dto.stage.StageDto;
//import com.quanlydoantotnghiep.DoAnTotNghiep.dto.stage.StageStatusDto;
//import com.quanlydoantotnghiep.DoAnTotNghiep.dto.stage.request.CreateStageRequest;
//import com.quanlydoantotnghiep.DoAnTotNghiep.entity.*;
//import com.quanlydoantotnghiep.DoAnTotNghiep.exception.ApiException;
//import com.quanlydoantotnghiep.DoAnTotNghiep.repository.*;
//import com.quanlydoantotnghiep.DoAnTotNghiep.service.StageService;
//import lombok.RequiredArgsConstructor;
//import org.modelmapper.ModelMapper;
//import org.springframework.data.domain.Sort;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class StageServiceImpl implements StageService {
//
//    private final StageRepository stageRepository;
//    private final TeacherRepository teacherRepository;
//    private final SemesterRepository semesterRepository;
//    private final ProjectRepository projectRepository;
//    private final ProjectStageRepository projectStageRepository;
//    private final StageStatusRepository stageStatusRepository;
//    private final StageFileRepository stageFileRepository;
//    private final AccountRepository accountRepository;
//    private final ModelMapper modelMapper;
//
//    @Transactional
//    @Override
//    public StageDto createStage(AccountDto accountDto, CreateStageRequest createStageRequest) {
//
//        // get teacher who created this stage:
//        Teacher teacher = teacherRepository.findByAccount_Code(accountDto.getCode())
//                            .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Teacher is not exists with given code: "+accountDto.getCode()));
//
//        // get current semester:
//        Semester currentSemester = semesterRepository.findByIsCurrentIsTrue();
//
//        // get max of stageOrder: Lấy ra thứ tự lớn nhất của stage (là tk cuối cùng):
//        Integer maxStageOrder = stageRepository.findMaxStageOrder(teacher.getAccount().getCode(), currentSemester.getSemesterId());
//        Integer newStageOrder = maxStageOrder + 1;
//
//        // get StageStatus is "Chưa mở" by default when created:
//        StageStatus defaultStageStatus = stageStatusRepository.findById(1L)
//                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Stage status is not exists with given id: "+1L));
//
//        // create stage:
//        Stage stage = Stage.builder()
//                .stageName(createStageRequest.getStageName())
//                .stageTitle(createStageRequest.getStageTitle())
//                .stageContent(createStageRequest.getStageContent())
//                .stageOrder(newStageOrder)
//                .startDate(createStageRequest.getStartDate())
//                .endDate(createStageRequest.getEndDate())
//                .semester(currentSemester)
//                .teacher(teacher)
//                .stageStatus(defaultStageStatus)
//                .build();
//
//        List<StageFile> stageFiles = createStageRequest.getStageFiles().stream()
//                .map((item) -> {
//                   return StageFile.builder()
//                            .nameFile(item.getNameFile())
//                            .pathFile(item.getPathFile())
//                            .stage(stage).build();
//                }).collect(Collectors.toList());
//
//        stage.setStageFiles(stageFiles);
//
//        Stage savedStage = stageRepository.save(stage);
//
//        // add created stage to all projects that were managed by teacher
//        applyStageToAllProjectsOfTeacher(savedStage, teacher, defaultStageStatus);
//
//        // convert to stageDto:
//        return convertToStageDto(savedStage);
//    }
//
//    private void applyStageToAllProjectsOfTeacher(Stage stage, Teacher teacher, StageStatus defaultStageStatus) {
//
//        // get all projects by teacherCode and semesterId
//        List<Project> projects = projectRepository
//                .findAllProjectsByTeacherAndSemester(
//                        teacher.getAccount().getCode(),
//                        stage.getSemester().getSemesterId()
//                );
//
//        projects.forEach((item) -> {
//
//            ProjectStage projectStage = ProjectStage.builder()
//                    .project(item)
//                    .stage(stage)
//                    .stageStatus(defaultStageStatus).build();
//
//            // save project stage:
//            projectStageRepository.save(projectStage);
//        });
//
//    }
//
//    @Override
//    public StageDto updateStage(Long stageId, AccountDto accountDto, CreateStageRequest updateStageRequest) {
//
//        Account account = accountRepository.findById(accountDto.getAccountId())
//                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Account is not exists with given id: "+accountDto.getAccountId()));
//
//        // get stage:
//        Stage stage = stageRepository.findById(stageId)
//                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Stage is not exists with given id: "+stageId));
//
//        // check if stage is not managed by this teacher -> throw exception
//        if(!account.getTeacher().getTeacherId().equals(stage.getTeacher().getTeacherId()))
//            throw new ApiException(HttpStatus.BAD_REQUEST, "Stage is not valid!");
//
//        // update stage
//        stage.setStageName(updateStageRequest.getStageName());
//        stage.setStageTitle(updateStageRequest.getStageTitle());
//        stage.setStageContent(updateStageRequest.getStageContent());
//        stage.setStartDate(updateStageRequest.getStartDate());
//        stage.setEndDate(updateStageRequest.getEndDate());
//
//        // Delete all old stageFiles before add new
//        stage.getStageFiles().clear();
//
//        // add new list stageFiles
//        List<StageFile> stageFiles = updateStageRequest.getStageFiles().stream().map(
//                (item) -> {
//                    return StageFile.builder()
//                            .nameFile(item.getNameFile())
//                            .pathFile(item.getPathFile())
//                            .stage(stage)
//                            .build();
//                }
//        ).collect(Collectors.toList());
//
//        stage.getStageFiles().addAll(stageFiles);
//
//        Stage updatedStage = stageRepository.save(stage);
//
//        return convertToStageDto(updatedStage);
//    }
//
//    @Override
//    public String deleteStage(Long stageId, AccountDto accountDto) {
//
//        Account account = accountRepository.findById(accountDto.getAccountId())
//                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Account is not exists with given id: "+accountDto.getAccountId()));
//
//        // get stage:
//        Stage stage = stageRepository.findById(stageId)
//                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Stage is not exists with given id: "+stageId));
//
//        // check if stage is not managed by this teacher -> throw exception
//        if(!account.getTeacher().getTeacherId().equals(stage.getTeacher().getTeacherId()))
//            throw new ApiException(HttpStatus.BAD_REQUEST, "Stage is not valid!");
//
//        // delete stage
//        stageRepository.delete(stage);
//
//        return "Stage with id: "+stageId+" was deleted successfully";
//    }
//
//    @Override
//    public List<StageDto> getAllStagesByTeacherAndSemester(AccountDto accountDto, Long semesterId) {
//
//        // get teacher:
//        Teacher teacher = teacherRepository.findByAccount_Code(accountDto.getCode())
//                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Teacher is not exists with given code: "+accountDto.getCode()));
//
//        // get current semester :
//        Semester currentSemester = semesterRepository.findByIsCurrentIsTrue();
//
//        // Sort by stageOrder by ascending:
//        Sort sort = Sort.by("stageOrder").ascending();
//
//        // if semesterId is null -> filter by currentSemester
//        List<Stage> stages = stageRepository.findAllStagesByTeacherAndSemester(
//                teacher.getTeacherId(), semesterId != null ? semesterId : currentSemester.getSemesterId(), sort
//        );
//
//        return stages.stream().map((item) -> {
//
//            return convertToStageDto(item);
//        }).collect(Collectors.toList());
//    }
//
//    @Override
//    public StageDto getStageById(Long stageId) {
//
//        // get stage:
//        Stage stage = stageRepository.findById(stageId)
//                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Stage is not exists with given id: "+stageId));
//
//        return convertToStageDto(stage);
//    }
//
//    @Override
//    public String deleteStageFileById(Long stageFileId) {
//
//        StageFile stageFile = stageFileRepository.findById(stageFileId)
//                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Stage file is not exists with given id: "+stageFileId));
//
//        stageFileRepository.delete(stageFile);
//
//        return "Stage file with id: "+stageFile+" was deleted successfully";
//    }
//
//    @Transactional
//    @Override
//    public StageDto updateStageStatus(Long stageId, Long stageStatusId) {
//
//        // get stage
//        Stage stage = stageRepository.findById(stageId)
//                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Stage is not exists with given id: "+stageId));
//
//        // get stage status:
//        StageStatus newStageStatus = stageStatusRepository.findById(stageStatusId)
//                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Stage status is not exists with given id: "+stageStatusId));
//
//        stage.setStageStatus(newStageStatus);
//
//        Stage savedStage = stageRepository.save(stage);
//
//        savedStage.getProjectStages().forEach((item) -> {
//
//            item.setStageStatus(newStageStatus);
//            projectStageRepository.save(item);
//        });
//
//        return convertToStageDto(savedStage);
//    }
//
//    @Override
//    public List<StageStatusDto> getAllStageStatuses() {
//
//        List<StageStatus> stageStatuses = stageStatusRepository.findAll();
//
//        return stageStatuses.stream().map(
//                (item) -> modelMapper.map(item, StageStatusDto.class)
//        ).collect(Collectors.toList());
//    }
//
//    @Transactional
//    @Override
//    public List<StageDto> updateStageOrder(AccountDto accountDto, List<Long> newStageIds) {
//
//        Account account = accountRepository.findById(accountDto.getAccountId())
//                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Account is not exists with given id: "+accountDto.getAccountId()));
//
//        // get current semester :
//        Semester currentSemester = semesterRepository.findByIsCurrentIsTrue();
//
//        // Sort by stageOrder by ascending:
//        Sort sort = Sort.by("stageOrder").ascending();
//
//        // get list stage of teacher:
//        List<Stage> stages = stageRepository.findAllStagesByTeacherAndSemester(account.getTeacher().getTeacherId(), currentSemester.getSemesterId(), sort);
//
//        if(newStageIds.size() != stages.size())
//            throw new ApiException(HttpStatus.BAD_REQUEST, "Occur error when update stage order");
//
//        // update new stage order for stages:
//        List<StageDto> updatedStages = new ArrayList<>();
//
//        for(int i = 0; i < newStageIds.size(); i++) {
//
//            int index = i;
//            Stage stage = stageRepository.findById(newStageIds.get(index))
//                    .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Stage is not exists with given id: "+newStageIds.get(index)));
//
//            stage.setStageOrder(index+1);
//
//            Stage savedStage = stageRepository.save(stage);
//
//            updatedStages.add(convertToStageDto(savedStage));
//        }
//
//        return updatedStages;
//    }
//
//    private StageDto convertToStageDto(Stage item) {
//
//        StageDto stageDto = modelMapper.map(item, StageDto.class);
//        TeacherAccountResponse teacherAccountResponse = modelMapper.map(item.getTeacher().getAccount(), TeacherAccountResponse.class);
//        teacherAccountResponse.setTeacherCode(item.getTeacher().getAccount().getCode());
//        teacherAccountResponse.setDegree(
//                modelMapper.map(item.getTeacher().getDegree(), DegreeDto.class)
//        );
//        teacherAccountResponse.setFaculty(
//                modelMapper.map(item.getTeacher().getFaculty(), FacultyDto.class)
//        );
//        teacherAccountResponse.setLeader(item.getTeacher().isLeader());
//        stageDto.setTeacher(teacherAccountResponse);
//
//        return stageDto;
//    }
//
//}
