package com.quanlydoantotnghiep.DoAnTotNghiep.service.impl;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.*;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.StudentHavingInstructorDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.response.TeacherAccountResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.clazz.ClassDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.instructor.RecommendedTeacherDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.project.ProjectDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.project.ProjectFileDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.project.ProjectStatusDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.stage.StageDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.stage.StageStatusDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.*;
import com.quanlydoantotnghiep.DoAnTotNghiep.exception.ApiException;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.*;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.ClassService;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.InstructorService;
import com.quanlydoantotnghiep.DoAnTotNghiep.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstructorServiceImpl implements InstructorService {

    private final AccountRepository accountRepository;
    private final SemesterRepository semesterRepository;
    private final StudentRepository studentRepository;
    private final StudentSemesterRepository studentSemesterRepository;
    private final ProjectRepository projectRepository;
    private final ProjectStatusRepository projectStatusRepository;
    private final ProjectStageRepository projectStageRepository;
    private final ModelMapper modelMapper;
    private final StageRepository stageRepository;


    @Override
    public ProjectDto getProjectByStudentCodeAndSemester(String studentCode, Long semesterId) {

        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Semester is not exists with given id: "+semesterId));

        // get project by student and current semester
        Project project = projectRepository.findProjectByStudentAndSemester(studentCode, semester.getSemesterId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Project is not exists with given studentCode: "+studentCode+" in semester "+semester.getSemesterName()));

        StudentSemester studentSemester = project.getStudentSemester();

        // convert to ProjectDto:
        return convertToProjectDto(project, studentSemester);
    }

    @Override
    public ProjectDto getProjectByProjectId(Long projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Project is not exists with given id: "+projectId));

        return convertToProjectDto(project, project.getStudentSemester());
    }

    @Override
    public ObjectResponse getAllStudentsOfInstructor(AccountDto accountDto, String keyword, Long semesterId, Long classId, Boolean havingProject, int pageNumber, int pageSize, String sortBy, String sortDir) {

        Account account = accountRepository.findById(accountDto.getAccountId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Account is not exists with given id: "+accountDto.getAccountId()));

        // get teacher code (instructor code)
        String teacherCode = account.getTeacher().getAccount().getCode();

         Semester semester = (semesterId == null)
                ? semesterRepository.findByIsCurrentIsTrue() // get current semester
                : semesterRepository.findById(semesterId) // else get semester by specific semesterId
                    .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Semester is not exists with given id: " + semesterId));

        // create pageable
        Pageable pageable = AppUtils.createPageable(pageNumber, pageSize, sortBy, sortDir);

        // if have semesterId -> filter by specific semesterId, else -> filter by semesterId of current semester
        Page<Student> pageStudents = studentRepository
                .findAllStudentsByInstructorAndSemester(keyword, semester.getSemesterId(), classId, havingProject, teacherCode, pageable);

        List<StudentHavingInstructorDto> students = pageStudents.getContent().stream()
                .map((student) -> {

                    // get studentSemester for converting to StudentDto
                    StudentSemester studentSemester = studentSemesterRepository
                                                        .findByStudentStudentIdAndSemesterSemesterId(student.getStudentId(), semester.getSemesterId());

                    return convertToStudentDto(student, studentSemester);
                }).collect(Collectors.toList());

        return AppUtils.createObjectResponse(pageStudents, students);
    }

    @Override
    public ObjectResponse getAllProjectsManagedByInstructor(AccountDto accountDto, String keyword, Long semesterId, Long classId, int pageNumber, int pageSize, String sortBy, String sortDir) {

        Account account = accountRepository.findById(accountDto.getAccountId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Account is not exists with given id: "+accountDto.getAccountId()));

        // get teacher code (instructor code)
        String teacherCode = account.getTeacher().getAccount().getCode();

        Semester semester = (semesterId == null)
                ? semesterRepository.findByIsCurrentIsTrue() // get current semester
                : semesterRepository.findById(semesterId) // else get semester by specific semesterId
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Semester is not exists with given id: " + semesterId));

        // create pageable
        Pageable pageable = AppUtils.createPageable(pageNumber, pageSize, sortBy, sortDir);

        Page<Project> pageProject = projectRepository.findAllProjectsByInstructorAndSemester(keyword, semester.getSemesterId(), classId, teacherCode, pageable);

        List<ProjectDto> projectDtos = pageProject.getContent().stream()
                .map((project) -> {

                    return convertToProjectDto(project, project.getStudentSemester());
                }).collect(Collectors.toList());

        return AppUtils.createObjectResponse(pageProject, projectDtos);
    }

    @Override
    public ProjectDto approveProject(Long projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Project is not exists with given id: "+projectId));

        // after approve project -> change project status to 2 (Đang thực hiện)
        ProjectStatus projectStatus = projectStatusRepository.findById(2L)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Project status is not exists with given id: "+2));

        project.setProjectStatus(projectStatus);

        Project savedProject = projectRepository.save(project);

        return convertToProjectDto(savedProject, savedProject.getStudentSemester());
    }

    @Override
    public ProjectDto declineProject(Long projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Project is not exists with given id: "+projectId));

        // after decline project -> change project status to 3 (Bị từ chối)
        ProjectStatus projectStatus = projectStatusRepository.findById(3L)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Project status is not exists with given id: "+3));

        project.setProjectStatus(projectStatus);

        Project savedProject = projectRepository.save(project);

        return convertToProjectDto(savedProject, savedProject.getStudentSemester());
    }

    @Override
    public List<StageDto> getAllStagesByProject(Long projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Project is not exists with given id: "+projectId));

        // sort by stageOrder by ascending:
        Sort sort = Sort.by("stage.stageOrder").ascending();

        // get List ProjectStage by projectId
        List<ProjectStage> projectStages = projectStageRepository.findAllByProjectProjectId(projectId, sort);

        // convert to list StageDto:
        List<StageDto> stageDtos = projectStages.stream().map(
                (projectStage) -> {

                    Stage stage = projectStage.getStage();
                    StageStatus stageStatus = projectStage.getStageStatus();

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

    private ProjectDto convertToProjectDto(Project project, StudentSemester studentSemester) {

        // InProgress Stage
        ProjectStage inProgressProjectStage = projectStageRepository.findInProgressProjectStageByProjectId(project.getProjectId());

        ProjectDto projectDto = ProjectDto.builder()
                .projectId(project.getProjectId())
                .projectName(project.getProjectName())
                .projectContent(project.getProjectContent())
                .projectStatus(modelMapper.map(project.getProjectStatus(), ProjectStatusDto.class))
                .projectFiles(project.getProjectFiles().stream()
                    .map((item) -> modelMapper.map(item, ProjectFileDto.class)).collect(Collectors.toList())
                )
                .inProgressStage(
                        inProgressProjectStage!=null ? inProgressProjectStage.getStage().getStageName() : null
                )
                .numberOfCompletedStages(projectStageRepository.countAllCompletedStagesByProject(project.getProjectId()))
                .totalStages(projectStageRepository.countByProjectProjectId(project.getProjectId()))
                .totalCreatedStages(stageRepository.countByTeacherTeacherIdAndSemesterSemesterId(
                        project.getTeacher().getTeacherId(),
                        studentSemester.getSemester().getSemesterId()
                ))
                .createdAt(project.getCreatedAt())
                .build();

        projectDto.setSemester(modelMapper.map(studentSemester.getSemester(), SemesterDto.class));

        StudentDto studentDto = modelMapper.map(studentSemester.getStudent().getAccount(), StudentDto.class);
        studentDto.setStudentCode(studentSemester.getStudent().getAccount().getCode());
        studentDto.setStudentId(studentSemester.getStudent().getStudentId());
        studentDto.setStudentClass(modelMapper.map(studentSemester.getStudent().getClazz(), ClassDto.class));
        studentDto.setSemester(
                modelMapper.map(studentSemester.getSemester(), SemesterDto.class)
        );

        studentDto.setRecommendedTeachers(
                studentSemester.getStudent().getProposedTeachers().stream()
                        .filter((item) -> item.getSemester().getSemesterId().equals(studentSemester.getSemester().getSemesterId())) // phải lọc ra các đề xuất của sinh viên theo học kỳ hiện tại
                        // (nếu không lọc thì nó sẽ lấy full các đề xuất của sinh viên đó trong tất cả các học kỳ)
                        .map((item) -> {

                            RecommendedTeacherDto recommendedTeacherDto = RecommendedTeacherDto.builder()
                                    .teacherId(item.getTeacher().getTeacherId())
                                    .teacherCode(item.getTeacher().getAccount().getCode())
                                    .teacherName(item.getTeacher().getAccount().getFullName())
                                    .build();

                            return recommendedTeacherDto;
                        }).sorted((o1, o2) -> o1.getTeacherName().toLowerCase().compareTo(o2.getTeacherName().toLowerCase())).collect(Collectors.toList())
        );

        if(studentSemester.getInstructor()!=null)
        {
            TeacherAccountResponse teacherAccountResponse = modelMapper.map(studentSemester.getInstructor().getAccount(), TeacherAccountResponse.class);
            teacherAccountResponse.setTeacherCode(studentSemester.getInstructor().getAccount().getCode());
            teacherAccountResponse.setFaculty(modelMapper.map(studentSemester.getInstructor().getFaculty(), FacultyDto.class));
            teacherAccountResponse.setDegree(modelMapper.map(studentSemester.getInstructor().getDegree(), DegreeDto.class));
            teacherAccountResponse.setLeader(studentSemester.getInstructor().isLeader());

            studentDto.setInstructor(teacherAccountResponse);
        }

        projectDto.setStudent(studentDto);

        return projectDto;
    }

    private StudentHavingInstructorDto convertToStudentDto(Student student, StudentSemester studentSemester) {

        StudentHavingInstructorDto studentDto = modelMapper.map(student.getAccount(), StudentHavingInstructorDto.class);
        studentDto.setStudentCode(student.getAccount().getCode());
        studentDto.setStudentId(student.getStudentId());
        studentDto.setStudentClass(modelMapper.map(student.getClazz(), ClassDto.class));

        studentDto.setSemester(
                modelMapper.map(studentSemester.getSemester(), SemesterDto.class)
        );

        studentDto.setRecommendedTeachers(
                student.getProposedTeachers().stream()
                        .filter((item) -> item.getSemester().getSemesterId().equals(studentSemester.getSemester().getSemesterId())) // phải lọc ra các đề xuất của sinh viên theo học kỳ hiện tại
                        // (nếu không lọc thì nó sẽ lấy full các đề xuất của sinh viên đó trong tất cả các học kỳ)
                        .map((item) -> {

                            RecommendedTeacherDto recommendedTeacherDto = RecommendedTeacherDto.builder()
                                    .teacherId(item.getTeacher().getTeacherId())
                                    .teacherCode(item.getTeacher().getAccount().getCode())
                                    .teacherName(item.getTeacher().getAccount().getFullName())
                                    .build();

                            return recommendedTeacherDto;
                        }).sorted((o1, o2) -> o1.getTeacherName().toLowerCase().compareTo(o2.getTeacherName().toLowerCase())).collect(Collectors.toList())
        );

        if(studentSemester.getProject()!=null)
        {
            StudentProjectDto projectDto = modelMapper.map(studentSemester.getProject(), StudentProjectDto.class);
            studentDto.setProject(projectDto);
        }

        if(studentSemester.getInstructor()!=null)
        {
            TeacherAccountResponse teacherAccountResponse = modelMapper.map(studentSemester.getInstructor().getAccount(), TeacherAccountResponse.class);
            teacherAccountResponse.setTeacherCode(studentSemester.getInstructor().getAccount().getCode());
            teacherAccountResponse.setFaculty(modelMapper.map(studentSemester.getInstructor().getFaculty(), FacultyDto.class));
            teacherAccountResponse.setDegree(modelMapper.map(studentSemester.getInstructor().getDegree(), DegreeDto.class));
            teacherAccountResponse.setLeader(studentSemester.getInstructor().isLeader());

            studentDto.setInstructor(teacherAccountResponse);
        }

        return studentDto;
    }
}
