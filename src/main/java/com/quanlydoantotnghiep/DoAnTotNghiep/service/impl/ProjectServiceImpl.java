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
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.project.ProjectStatusDto;
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
    private final StudentSemesterRepository studentSemesterRepository;
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

        // get current semester
        Semester currentSemester = semesterRepository.findByIsCurrentIsTrue();

        StudentSemester studentSemester = studentSemesterRepository.findByStudentStudentIdAndSemesterSemesterId(student.getStudentId(), currentSemester.getSemesterId());

        if(studentSemester==null)
            throw new ApiException(HttpStatus.BAD_REQUEST, "Student doesn't exists in semester: "+currentSemester.getSemesterName());

        if(studentSemester.getInstructor()==null)
            throw new ApiException(HttpStatus.BAD_REQUEST, "Student doesn't have instructor in semester: "+currentSemester.getSemesterName());

        Project project = Project.builder()
                .projectName(createProjectRequest.getProjectName())
                .projectContent(createProjectRequest.getProjectContent())
                .projectStatus(projectStatus)
                .flagDelete(false)
                .studentSemester(studentSemester)
                .teacher(studentSemester.getInstructor())
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
        return convertToProjectDto(savedProject, studentSemester, currentSemester);
    }

    @Override
    public ProjectDto getProjectByStudentCode(String studentCode) {

        // get current semester:
        Semester currentSemester = semesterRepository.findByIsCurrentIsTrue();

        // get project by student and current semester
        Project project = projectRepository.findProjectByStudentAndSemester(studentCode, currentSemester.getSemesterId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Project is not exists with given studentCode: "+studentCode+" in semester "+currentSemester.getSemesterName()));

        StudentSemester studentSemester = project.getStudentSemester();

        // convert to ProjectDto:
        return convertToProjectDto(project, studentSemester, currentSemester);
    }

    @Override
    public ProjectDto updateProject(Long projectId, AccountDto accountDto, CreateProjectRequest updateProjectRequest) {

        Account account = accountRepository.findById(accountDto.getAccountId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Account is not exists with given id: "+accountDto.getAccountId()));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Project is not exists with given id: "+projectId));

        ProjectStatus projectStatus = projectStatusRepository.findById(1L)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Project status is not exists with given id: "+1));

        // check nếu project này không phải của student này thì không cho update (tránh student khác update project của student này)
        if(!account.getStudent().getStudentId().equals(project.getStudentSemester().getStudent().getStudentId()))
            throw new ApiException(HttpStatus.BAD_REQUEST, "Project is not valid!");

        // Only project with status id: 1 (Chờ duyệt) and 4 (Bị từ chối) is accepted
        if(project.getProjectStatus().getProjectStatusId() != 1 && project.getProjectStatus().getProjectStatusId() != 4)
            throw new ApiException(HttpStatus.BAD_REQUEST, "Only project with status id: 1 (Chờ duyệt) and 4 (Bị từ chối) is accepted for using function updateProject");

        project.setProjectName(updateProjectRequest.getProjectName());
        project.setProjectContent(updateProjectRequest.getProjectContent());

        // after update project -> set project status = 1 (Chờ duyệt)
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

        return convertToProjectDto(savedProject, savedProject.getStudentSemester(), savedProject.getStudentSemester().getSemester());
    }

//    @Override
//    public String deleteProjectById(Long projectId) {
//
//        Project project = projectRepository.findById(projectId)
//                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Project is not exists with given id: "+projectId));
//
//        projectRepository.delete(project);
//
//        return "Delete project with id: "+projectId+" successfully";
//    }

    @Override
    public String deleteProjectFileByProjectFileId(Long projectFileId) {

        ProjectFile projectFile = projectFileRepository.findById(projectFileId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "ProjectFile is not exists with given id: "+projectFileId));

        // Only project with status id: 1 (Chờ duyệt) and 4 (Bị từ chối) is accepted
        if(projectFile.getProject().getProjectStatus().getProjectStatusId() != 1 && projectFile.getProject().getProjectStatus().getProjectStatusId() != 4)
            throw new ApiException(HttpStatus.BAD_REQUEST, "Only project with status id: 1 (Chờ duyệt) and 4 (Bị từ chối) is accepted for using function deleteProjectFile");

        projectFileRepository.delete(projectFile);

        return "ProjectFile with id: "+projectFileId+" was deleted successfully";
    }

    private ProjectDto convertToProjectDto(Project project, StudentSemester studentSemester, Semester currentSemester) {

        ProjectDto projectDto = ProjectDto.builder()
                .projectId(project.getProjectId())
                .projectName(project.getProjectName())
                .projectContent(project.getProjectContent())
                .projectFiles(
                        project.getProjectFiles().stream()
                                .map((item) -> modelMapper.map(item, ProjectFileDto.class)).collect(Collectors.toList()))
                .projectStatus(modelMapper.map(project.getProjectStatus(), ProjectStatusDto.class))
                .createdAt(project.getCreatedAt())
                .build();

        projectDto.setSemester(modelMapper.map(studentSemester.getSemester(), SemesterDto.class));

        StudentDto studentDto = modelMapper.map(studentSemester.getStudent().getAccount(), StudentDto.class);
        studentDto.setStudentCode(studentSemester.getStudent().getAccount().getCode());
        studentDto.setStudentId(studentSemester.getStudent().getStudentId());
        studentDto.setStudentClass(modelMapper.map(studentSemester.getStudent().getClazz(), ClassDto.class));



        studentDto.setRecommendedTeachers(
                studentSemester.getStudent().getProposedTeachers().stream()
                        .filter((item) -> item.getSemester().getSemesterId().equals(currentSemester.getSemesterId())) // phải lọc ra các đề xuất của sinh viên theo học kỳ hiện tại
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
}