package com.quanlydoantotnghiep.DoAnTotNghiep.service.impl;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.DegreeDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.FacultyDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.SemesterDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.StudentDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.response.TeacherAccountResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.clazz.ClassDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.instructor.RecommendedTeacherDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.project.ProjectDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.project.ProjectFileDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.project.request.CreateProjectRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.*;
import com.quanlydoantotnghiep.DoAnTotNghiep.exception.ApiException;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.*;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectFileRepository projectFileRepository;
    private final ProjectStatusRepository projectStatusRepository;
    private final StudentRepository studentRepository;
    private final SemesterRepository semesterRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    @Override
    public ProjectDto createProject(CreateProjectRequest createProjectRequest) {

        // Project status default is "Chưa duyệt" when is just created
        ProjectStatus projectStatus = projectStatusRepository.findById(1L)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Project status is not exists with given id: "+1L));

        // get student
        Student student = studentRepository.findByAccount_Code(createProjectRequest.getStudentCode())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Student is not exists with given code: "+createProjectRequest.getStudentCode()));

        if(student.getSemesters().size() < 0)
            throw new ApiException(HttpStatus.BAD_REQUEST, "Semester is not valid");

        if(student.getInstructor()==null)
            throw new ApiException(HttpStatus.BAD_REQUEST, "Student doesn't have instructor");

        // get current semester:
        Semester currentSemester = semesterRepository.findByIsCurrentIsTrue();

        Project project = Project.builder()
                .projectName(createProjectRequest.getProjectName())
                .projectContent(createProjectRequest.getProjectContent())
                .projectStatus(projectStatus)
                .flagDelete(false)
                .student(student)
                .semester(currentSemester)
                .teacher(student.getInstructor())
                .build();

        List<ProjectFile> projectFiles = createProjectRequest.getProjectFiles().stream().map((item) -> {

            return ProjectFile.builder()
                    .nameFile(item.getNameFile())
                    .pathFile(item.getPathFile())
                    .project(project)
                    .build();
        }).collect(Collectors.toList());

        project.setProjectFiles(projectFiles);

        // save project
        Project savedProject = projectRepository.save(project);

        // convert to ProjectDto:
        return convertToProjectDto(savedProject, student);
    }

    @Override
    public ProjectDto getProjectByStudentCode(String studentCode) {

        Project project = projectRepository.findByStudent_Account_Code(studentCode)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Project is not exists with given studentCode: "+studentCode));

        Student student = project.getStudent();

        // convert to ProjectDto:
        return convertToProjectDto(project, student);
    }

    @Override
    public ProjectDto updateProject(Long projectId, AccountDto accountDto, CreateProjectRequest updateProjectRequest) {

        Account account = accountRepository.findById(accountDto.getAccountId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Account is not exists with given id: "+accountDto.getAccountId()));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Project is not exists with given id: "+projectId));

        ProjectStatus projectStatus = projectStatusRepository.findById(1L)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Project status is not exists with given id: "+1));

        if(!account.getStudent().getStudentId().equals(project.getStudent().getStudentId()))
            throw new ApiException(HttpStatus.BAD_REQUEST, "Project is not valid!");

        // Only project with status id: 1 (Chờ duyệt) and 4 (Bị từ chối) is accepted
        if(project.getProjectStatus().getProjectStatusId() != 1 && project.getProjectStatus().getProjectStatusId() != 4)
            throw new ApiException(HttpStatus.BAD_REQUEST, "Only project with status id: 1 (Chờ duyệt) and 4 (Bị từ chối) is accepted for using function updateProject");

        project.setProjectName(updateProjectRequest.getProjectName());
        project.setProjectContent(updateProjectRequest.getProjectContent());

        // when update project -> set project status = 1 (Chờ duyệt)
        project.setProjectStatus(projectStatus);

        // Delete all old projectFiles before add new
        project.getProjectFiles().clear();

        // add new list projectFiles
        List<ProjectFile> newProjectFiles = updateProjectRequest.getProjectFiles().stream().map(
                (item) -> {
                    return ProjectFile.builder()
                            .nameFile(item.getNameFile())
                            .pathFile(item.getPathFile())
                            .project(project)
                            .build();
                }
        ).collect(Collectors.toList());

        project.getProjectFiles().addAll(newProjectFiles);

        Project savedProject = projectRepository.save(project);

        return convertToProjectDto(savedProject, savedProject.getStudent());
    }

    @Override
    public String deleteProjectById(Long projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Project is not exists with given id: "+projectId));

        projectRepository.delete(project);

        return "Delete project with id: "+projectId+" successfully";
    }

    @Override
    public String deleteProjectFileByProjectFileId(Long projectFileId) {

        ProjectFile projectFile = projectFileRepository.findById(projectFileId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "ProjectFile is not exists with given id: "+projectFileId));

        // Only project with status id: 1 is accepted
        if(projectFile.getProject().getProjectStatus().getProjectStatusId() != 1)
            throw new ApiException(HttpStatus.BAD_REQUEST, "Only project with status id: 1 (Chờ xét duyệt) is accepted for using function updateProject");

        projectFileRepository.delete(projectFile);

        return "ProjectFile with id: "+projectFileId+" was deleted successfully";
    }

    private ProjectDto convertToProjectDto(Project project, Student student) {
        ProjectDto projectDto = modelMapper.map(project, ProjectDto.class);

        StudentDto studentDto = modelMapper.map(project.getStudent().getAccount(), StudentDto.class);
        studentDto.setStudentCode(student.getAccount().getCode());
        studentDto.setStudentId(student.getStudentId());
        studentDto.setStudentClass(modelMapper.map(student.getClazz(), ClassDto.class));
        studentDto.setSemesters(
                student.getSemesters().stream().map(
                        item -> modelMapper.map(item, SemesterDto.class)
                ).collect(Collectors.toSet())
        );

        studentDto.setRecommendedTeachers(
                student.getTeachers().stream().map((item) -> {

                    RecommendedTeacherDto recommendedTeacherDto = RecommendedTeacherDto.builder()
                            .teacherId(item.getTeacherId())
                            .teacherCode(item.getAccount().getCode())
                            .teacherName(item.getAccount().getFullName())
                            .build();

                    return recommendedTeacherDto;
                }).collect(Collectors.toList())
        );

        if(student.getInstructor()!=null)
        {
            TeacherAccountResponse teacherAccountResponse = modelMapper.map(student.getInstructor().getAccount(), TeacherAccountResponse.class);
            teacherAccountResponse.setTeacherCode(student.getInstructor().getAccount().getCode());
            teacherAccountResponse.setFaculty(modelMapper.map(student.getInstructor().getFaculty(), FacultyDto.class));
            teacherAccountResponse.setDegree(modelMapper.map(student.getInstructor().getDegree(), DegreeDto.class));
            teacherAccountResponse.setLeader(student.getInstructor().isLeader());

            studentDto.setInstructor(teacherAccountResponse);
        }

        projectDto.setStudent(studentDto);

        return projectDto;
    }
}