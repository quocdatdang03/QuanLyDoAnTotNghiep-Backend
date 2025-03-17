package com.quanlydoantotnghiep.DoAnTotNghiep.service.impl;

import com.quanlydoantotnghiep.DoAnTotNghiep.constant.AppConstant;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.*;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.response.TeacherAccountResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.clazz.ClassDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.instructor.RecommendedTeacherDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.*;
import com.quanlydoantotnghiep.DoAnTotNghiep.exception.ApiException;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.AccountRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.SemesterRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.StudentRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.TeacherRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.ClassService;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.InstructorLeaderService;
import com.quanlydoantotnghiep.DoAnTotNghiep.utils.AppUtils;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class InstructorLeaderServiceImpl implements InstructorLeaderService {

    private final SemesterRepository semesterRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final AccountRepository accountRepository;
    private final ClassService classService;
    private final ModelMapper modelMapper;

    // Tìm kiếm dssv theo khoa của giảng viên và học kỳ hiện tại, dssv mà chưa được phân GVHD
    @Override
    public ObjectResponse getAllStudentsWithoutInstructor(String keyword, Long classId, AccountDto accountDto, int pageNumber, int pageSize, String sortBy, String sortDir) {

        Account account = accountRepository.findById(accountDto.getAccountId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Account is not exists with given id: "+accountDto.getAccountId()));

        // Get faculty id
        Long facultyId = account.getTeacher().getFaculty().getFacultyId();

        // get current semester
        Semester semester = semesterRepository.findByIsCurrentIsTrue();

        // create pageable
        Pageable pageable = AppUtils.createPageable(pageNumber, pageSize, sortBy, sortDir);

        Page<Student> pageStudents = studentRepository
                .findAllStudentsWithoutInstructor(keyword, semester.getSemesterId(), facultyId, classId, pageable);

        List<StudentDto> students = pageStudents.getContent().stream()
                .map((student) -> {
                    StudentDto studentDto = modelMapper.map(student.getAccount(), StudentDto.class);
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

                    return studentDto;
                }).collect(Collectors.toList());

        return AppUtils.createObjectResponse(pageStudents, students);
    }

    @Override
    public ObjectResponse getAllStudentsHavingInstructor(String keyword, Long classId, String instructorCode, AccountDto accountDto, int pageNumber, int pageSize, String sortBy, String sortDir) {

        Account account = accountRepository.findById(accountDto.getAccountId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Account is not exists with given id: "+accountDto.getAccountId()));

        // Get faculty id
        Long facultyId = account.getTeacher().getFaculty().getFacultyId();

        // get current semester
        Semester semester = semesterRepository.findByIsCurrentIsTrue();

        // create pageable
        Pageable pageable = AppUtils.createPageable(pageNumber, pageSize, sortBy, sortDir);

        Page<Student> pageStudents = studentRepository
                .findAllStudentsHavingInstructor(keyword, semester.getSemesterId(), facultyId, classId, instructorCode, pageable);

        List<StudentDto> students = pageStudents.getContent().stream()
                .map((student) -> {

                    return convertToStudentDto(student);
                }).collect(Collectors.toList());

        return AppUtils.createObjectResponse(pageStudents, students);
    }

    @Override
    public TeacherAccountResponse getInstructorByStudentCode(String studentCode) {

        Student student = studentRepository.findByAccount_Code(studentCode)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Student is not exists with given code: "+studentCode));

        Teacher instructor = student.getInstructor();

        if(instructor==null)
            throw new ApiException(HttpStatus.BAD_REQUEST, "Student doesn't have instructor");

        // convert to TeacherAccountResponse:
        TeacherAccountResponse teacherAccountResponse = modelMapper.map(instructor.getAccount(), TeacherAccountResponse.class);
        teacherAccountResponse.setTeacherCode(instructor.getAccount().getCode());
        teacherAccountResponse.setDegree(
                modelMapper.map(instructor.getDegree(), DegreeDto.class)
        );
        teacherAccountResponse.setFaculty(
                modelMapper.map(instructor.getFaculty(), FacultyDto.class)
        );
        teacherAccountResponse.setLeader(instructor.isLeader());

        return teacherAccountResponse;
    }

    @Override
    public List<com.quanlydoantotnghiep.DoAnTotNghiep.dto.ClassDto> getAllClassesByFaculty(AccountDto accountDto) {

        Account account = accountRepository.findById(accountDto.getAccountId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Account is not exists with given id: "+accountDto.getAccountId()));

        // Get faculty id
        Long facultyId = account.getTeacher().getFaculty().getFacultyId();

        return classService.getAllClassesByFacultyId(facultyId);
    }


    @Override
    public List<TeacherAccountResponse> getAllTeachersByFaculty(AccountDto accountDto) {

        Account account = accountRepository.findById(accountDto.getAccountId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Account is not exists with given id: "+accountDto.getAccountId()));

        // get faculty id
        Long facultyId = account.getTeacher().getFaculty().getFacultyId();

        // get all teachers by faculty id:
        Sort sort = Sort.by(AppConstant.TEACHER_DEFAULT_SORT_BY).ascending();
        List<Teacher> teachers = teacherRepository.findByFacultyFacultyId(facultyId, sort);

        return teachers.stream().map(
                item -> {
                    TeacherAccountResponse teacherAccountResponse = modelMapper.map(item.getAccount(), TeacherAccountResponse.class);
                    teacherAccountResponse.setTeacherCode(item.getAccount().getCode());
                    teacherAccountResponse.setDegree(
                            modelMapper.map(item.getDegree(), DegreeDto.class)
                    );
                    teacherAccountResponse.setFaculty(
                            modelMapper.map(item.getFaculty(), FacultyDto.class)
                    );
                    teacherAccountResponse.setLeader(item.isLeader());

                    return teacherAccountResponse;
                }
        ).collect(Collectors.toList());
    }

    @Override
    public String assignInstructorForStudents(String teacherCode, List<String> studentCodeList) {

        Teacher teacher = teacherRepository.findByAccount_Code(teacherCode)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Teacher is not exists with given code:"+teacherCode));

        List<Student> students = studentRepository.findAllByAccount_CodeIn(studentCodeList);

        if(students.size() <= 0)
            throw new ApiException(HttpStatus.BAD_REQUEST, "Student list is empty");

        students.forEach((item) -> {
            item.setInstructor(teacher);
        });

        studentRepository.saveAll(students);

        return "Assigning instructor for students successfully!";
    }

    @Override
    public StudentDto removeInstructorFromStudent(String studentCode, String instructorCode) {

        Student student = studentRepository.findByAccount_Code(studentCode)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Student is not exists with given code: "+studentCode));

        Teacher teacher = teacherRepository.findByAccount_Code(instructorCode)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Teacher is not exists with given code: "+instructorCode));

        student.setInstructor(null);
        Student savedStudent = studentRepository.save(student);

        // convert to DTO:
        return convertToStudentDto(savedStudent);
    }

    @Override
    public StudentDto changeInstructorOfStudent(String studentCode, String instructorCode) {

        Student student = studentRepository.findByAccount_Code(studentCode)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Student is not exists with given code: "+studentCode));

        Teacher teacher = teacherRepository.findByAccount_Code(instructorCode)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Teacher is not exists with given code: "+instructorCode));

        // check if student have already registered project -> set instructor of this project = this teacher
        if(student.getProject()!=null) {
            student.getProject().setTeacher(teacher);
        }

        student.setInstructor(teacher);
        Student savedStudent = studentRepository.save(student);

        // convert to DTO:
        return convertToStudentDto(savedStudent);
    }

    private StudentDto convertToStudentDto(Student student) {
        StudentDto studentDto = modelMapper.map(student.getAccount(), StudentDto.class);
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

        return studentDto;
    }


}
