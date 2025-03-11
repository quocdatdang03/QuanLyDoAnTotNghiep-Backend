package com.quanlydoantotnghiep.DoAnTotNghiep.service.impl;

import com.quanlydoantotnghiep.DoAnTotNghiep.constant.AppConstant;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.*;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.response.TeacherAccountResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.clazz.ClassDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.instructor.RecommendedTeacherDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.Account;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.Semester;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.Student;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.Teacher;
import com.quanlydoantotnghiep.DoAnTotNghiep.exception.ApiException;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.AccountRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.SemesterRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.StudentRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.TeacherRepository;
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
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class InstructorServiceImpl implements InstructorService {

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


}
