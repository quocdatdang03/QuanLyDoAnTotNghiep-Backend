//package com.quanlydoantotnghiep.DoAnTotNghiep.service.impl;
//
//import com.quanlydoantotnghiep.DoAnTotNghiep.dto.*;
//import com.quanlydoantotnghiep.DoAnTotNghiep.dto.StudentHavingInstructorDto;
//import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
//import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.response.TeacherAccountResponse;
//import com.quanlydoantotnghiep.DoAnTotNghiep.dto.clazz.ClassDto;
//import com.quanlydoantotnghiep.DoAnTotNghiep.dto.instructor.RecommendedTeacherDto;
//import com.quanlydoantotnghiep.DoAnTotNghiep.dto.project.ProjectDto;
//import com.quanlydoantotnghiep.DoAnTotNghiep.entity.*;
//import com.quanlydoantotnghiep.DoAnTotNghiep.exception.ApiException;
//import com.quanlydoantotnghiep.DoAnTotNghiep.repository.*;
//import com.quanlydoantotnghiep.DoAnTotNghiep.service.ClassService;
//import com.quanlydoantotnghiep.DoAnTotNghiep.service.InstructorService;
//import com.quanlydoantotnghiep.DoAnTotNghiep.utils.AppUtils;
//import lombok.RequiredArgsConstructor;
//import org.modelmapper.ModelMapper;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class InstructorServiceImpl implements InstructorService {
//
//    private final AccountRepository accountRepository;
//    private final SemesterRepository semesterRepository;
//    private final StudentRepository studentRepository;
//    private final ProjectRepository projectRepository;
//    private final ProjectStatusRepository projectStatusRepository;
//    private final ModelMapper modelMapper;
//
//
//    @Override
//    public ObjectResponse getAllStudentsOfInstructor(AccountDto accountDto, String keyword, Long semesterId, Long classId, Boolean havingProject, int pageNumber, int pageSize, String sortBy, String sortDir) {
//
//        Account account = accountRepository.findById(accountDto.getAccountId())
//                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Account is not exists with given id: "+accountDto.getAccountId()));
//
//        // get teacher code (instructor code)
//        String teacherCode = account.getTeacher().getAccount().getCode();
//
//        Semester currentSemester = null;
//        if (semesterId==null)
//            currentSemester = semesterRepository.findByIsCurrentIsTrue();
//
//        // create pageable
//        Pageable pageable = AppUtils.createPageable(pageNumber, pageSize, sortBy, sortDir);
//
//        // if have semesterId -> filter by specific semesterId, else -> filter by semesterId of current semester
//        Page<Student> pageStudents = studentRepository
//                .findAllStudentsByInstructor(keyword, semesterId==null ? currentSemester.getSemesterId() : semesterId, classId, havingProject, teacherCode, pageable);
//
//        List<StudentHavingInstructorDto> students = pageStudents.getContent().stream()
//                .map((student) -> {
//
//                    return convertToStudentDto(student);
//                }).collect(Collectors.toList());
//
//        return AppUtils.createObjectResponse(pageStudents, students);
//    }
//
//    @Override
//    public ObjectResponse getAllProjectsManagedByInstructor(AccountDto accountDto, String keyword, Long semesterId, Long classId, int pageNumber, int pageSize, String sortBy, String sortDir) {
//
//        Account account = accountRepository.findById(accountDto.getAccountId())
//                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Account is not exists with given id: "+accountDto.getAccountId()));
//
//        // get teacher code (instructor code)
//        String teacherCode = account.getTeacher().getAccount().getCode();
//
//        Semester currentSemester = null;
//        if (semesterId==null)
//            currentSemester = semesterRepository.findByIsCurrentIsTrue();
//
//        // create pageable
//        Pageable pageable = AppUtils.createPageable(pageNumber, pageSize, sortBy, sortDir);
//
//        Page<Project> pageProject = projectRepository.findAllProjectsByInstructor(keyword, semesterId==null ? currentSemester.getSemesterId() : semesterId, classId, teacherCode, pageable);
//
//        List<ProjectDto> projectDtos = pageProject.getContent().stream()
//                .map((project) -> {
//
//                    return convertToProjectDto(project, project.getStudent());
//                }).collect(Collectors.toList());
//
//        return AppUtils.createObjectResponse(pageProject, projectDtos);
//    }
//
//    @Override
//    public ProjectDto approveProject(Long projectId) {
//
//        Project project = projectRepository.findById(projectId)
//                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Project is not exists with given id: "+projectId));
//
//        ProjectStatus projectStatus = projectStatusRepository.findById(2L)
//                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Project status is not exists with given id: "+2));
//
//        project.setProjectStatus(projectStatus);
//
//        Project savedProject = projectRepository.save(project);
//
//        return convertToProjectDto(savedProject, savedProject.getStudent());
//    }
//
//    @Override
//    public ProjectDto declineProject(Long projectId) {
//
//        Project project = projectRepository.findById(projectId)
//                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Project is not exists with given id: "+projectId));
//
//        ProjectStatus projectStatus = projectStatusRepository.findById(4L)
//                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Project status is not exists with given id: "+4));
//
//        project.setProjectStatus(projectStatus);
//
//        Project savedProject = projectRepository.save(project);
//
//        return convertToProjectDto(savedProject, savedProject.getStudent());
//    }
//
//    private ProjectDto convertToProjectDto(Project project, Student student) {
//        ProjectDto projectDto = modelMapper.map(project, ProjectDto.class);
//
//        StudentDto studentDto = modelMapper.map(project.getStudent().getAccount(), StudentDto.class);
//        studentDto.setStudentCode(student.getAccount().getCode());
//        studentDto.setStudentId(student.getStudentId());
//        studentDto.setStudentClass(modelMapper.map(student.getClazz(), ClassDto.class));
//        studentDto.setSemesters(
//                student.getSemesters().stream().map(
//                        item -> modelMapper.map(item, SemesterDto.class)
//                ).collect(Collectors.toSet())
//        );
//
//        studentDto.setRecommendedTeachers(
//                student.getTeachers().stream().map((item) -> {
//
//                    RecommendedTeacherDto recommendedTeacherDto = RecommendedTeacherDto.builder()
//                            .teacherId(item.getTeacherId())
//                            .teacherCode(item.getAccount().getCode())
//                            .teacherName(item.getAccount().getFullName())
//                            .build();
//
//                    return recommendedTeacherDto;
//                }).collect(Collectors.toList())
//        );
//
//        if(student.getInstructor()!=null)
//        {
//            TeacherAccountResponse teacherAccountResponse = modelMapper.map(student.getInstructor().getAccount(), TeacherAccountResponse.class);
//            teacherAccountResponse.setTeacherCode(student.getInstructor().getAccount().getCode());
//            teacherAccountResponse.setFaculty(modelMapper.map(student.getInstructor().getFaculty(), FacultyDto.class));
//            teacherAccountResponse.setDegree(modelMapper.map(student.getInstructor().getDegree(), DegreeDto.class));
//            teacherAccountResponse.setLeader(student.getInstructor().isLeader());
//
//            studentDto.setInstructor(teacherAccountResponse);
//        }
//
//        projectDto.setStudent(studentDto);
//
//        return projectDto;
//    }
//
//    private StudentHavingInstructorDto convertToStudentDto(Student student) {
//        StudentHavingInstructorDto studentDto = modelMapper.map(student.getAccount(), StudentHavingInstructorDto.class);
//        studentDto.setStudentCode(student.getAccount().getCode());
//        studentDto.setStudentId(student.getStudentId());
//        studentDto.setStudentClass(modelMapper.map(student.getClazz(), ClassDto.class));
//        studentDto.setSemesters(
//                student.getSemesters().stream().map(
//                        item -> modelMapper.map(item, SemesterDto.class)
//                ).collect(Collectors.toSet())
//        );
//
//        studentDto.setRecommendedTeachers(
//                student.getTeachers().stream().map((item) -> {
//
//                    RecommendedTeacherDto recommendedTeacherDto = RecommendedTeacherDto.builder()
//                            .teacherId(item.getTeacherId())
//                            .teacherCode(item.getAccount().getCode())
//                            .teacherName(item.getAccount().getFullName())
//                            .build();
//
//                    return recommendedTeacherDto;
//                }).collect(Collectors.toList())
//        );
//
//        if(student.getProject()!=null)
//        {
//            StudentProjectDto projectDto = modelMapper.map(student.getProject(), StudentProjectDto.class);
//            studentDto.setProject(projectDto);
//        }
//
//        if(student.getInstructor()!=null)
//        {
//            TeacherAccountResponse teacherAccountResponse = modelMapper.map(student.getInstructor().getAccount(), TeacherAccountResponse.class);
//            teacherAccountResponse.setTeacherCode(student.getInstructor().getAccount().getCode());
//            teacherAccountResponse.setFaculty(modelMapper.map(student.getInstructor().getFaculty(), FacultyDto.class));
//            teacherAccountResponse.setDegree(modelMapper.map(student.getInstructor().getDegree(), DegreeDto.class));
//            teacherAccountResponse.setLeader(student.getInstructor().isLeader());
//
//            studentDto.setInstructor(teacherAccountResponse);
//        }
//
//        return studentDto;
//    }
//}
