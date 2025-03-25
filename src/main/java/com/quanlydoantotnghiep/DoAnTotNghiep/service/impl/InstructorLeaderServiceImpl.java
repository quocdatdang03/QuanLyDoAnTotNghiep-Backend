package com.quanlydoantotnghiep.DoAnTotNghiep.service.impl;

import com.quanlydoantotnghiep.DoAnTotNghiep.constant.AppConstant;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.*;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.response.TeacherAccountResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.clazz.ClassDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.instructor.RecommendedTeacherDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.*;
import com.quanlydoantotnghiep.DoAnTotNghiep.exception.ApiException;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.*;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class InstructorLeaderServiceImpl implements InstructorLeaderService {

    private final SemesterRepository semesterRepository;
    private final StudentRepository studentRepository;
    private final StudentSemesterRepository studentSemesterRepository;
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
        Semester currentSemester = semesterRepository.findByIsCurrentIsTrue();

        // create pageable
        Pageable pageable = AppUtils.createPageable(pageNumber, pageSize, sortBy, sortDir);

        Page<Student> pageStudents = studentRepository
                .findAllStudentsWithoutInstructor(keyword, currentSemester.getSemesterId(), facultyId, classId, pageable);

        List<StudentDto> students = pageStudents.getContent().stream()
                .map((student) -> {

                    return convertToStudentDto(student, currentSemester);
                }).collect(Collectors.toList());

        return AppUtils.createObjectResponse(pageStudents, students);
    }

    @Override
    public ObjectResponse getAllStudentsHavingInstructor(String keyword, Long classId, String instructorCode, AccountDto accountDto, int pageNumber, int pageSize, String sortBy, String sortDir) {

        Account account = accountRepository.findById(accountDto.getAccountId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Account is not exists with given id: "+accountDto.getAccountId()));

        // Get faculty id of teacher leader
        Long facultyId = account.getTeacher().getFaculty().getFacultyId();

        // get current semester
        Semester currentSemester = semesterRepository.findByIsCurrentIsTrue();

        // create pageable
        Pageable pageable = AppUtils.createPageable(pageNumber, pageSize, sortBy, sortDir);

        Page<Student> pageStudents = studentRepository
                .findAllStudentsHavingInstructor(keyword, currentSemester.getSemesterId(), facultyId, classId, instructorCode, pageable);

        List<StudentDto> students = pageStudents.getContent().stream()
                .map((student) -> {

                    return convertToStudentDto(student, currentSemester);
                }).collect(Collectors.toList());

        return AppUtils.createObjectResponse(pageStudents, students);
    }

    @Override
    public TeacherAccountResponse getInstructorByStudentCode(String studentCode) {

        Student student = studentRepository.findByAccount_Code(studentCode)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Student is not exists with given code: "+studentCode));

        // get current semester
        Semester currentSemester = semesterRepository.findByIsCurrentIsTrue();

        StudentSemester studentSemester = studentSemesterRepository.findByStudentStudentIdAndSemesterSemesterId(student.getStudentId(), currentSemester.getSemesterId());

        if(studentSemester==null)
            throw new ApiException(HttpStatus.BAD_REQUEST, "Student doesn't exists in semester: "+currentSemester.getSemesterName());

        Teacher instructor = studentSemester.getInstructor();

        if(instructor==null)
            throw new ApiException(HttpStatus.BAD_REQUEST, "Student doesn't have instructor in semester "+currentSemester.getSemesterName());

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
    public List<ClassDto> getAllClassesByFaculty(AccountDto accountDto) {

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

    @Transactional
    @Override
    public String assignInstructorForStudents(String teacherCode, List<String> studentCodeList) {

        Teacher teacher = teacherRepository.findByAccount_Code(teacherCode)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Teacher is not exists with given code:"+teacherCode));

        List<Student> students = studentRepository.findAllByAccount_CodeIn(studentCodeList);

        // get current semester:
        Semester currentSemester = semesterRepository.findByIsCurrentIsTrue();

        if(students.size() <= 0)
            throw new ApiException(HttpStatus.BAD_REQUEST, "Student list is empty");

        students.forEach((item) -> {

            StudentSemester studentSemester = studentSemesterRepository.findByStudentStudentIdAndSemesterSemesterId(item.getStudentId(), currentSemester.getSemesterId());

            if(studentSemester!=null)
            {
                studentSemester.setInstructor(teacher);

                studentSemesterRepository.save(studentSemester);
            }
        });

        return "Assigning instructor for students successfully!";
    }

    @Transactional
    @Override
    public StudentDto removeInstructorFromStudent(String studentCode, String instructorCode) {

        Student student = studentRepository.findByAccount_Code(studentCode)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Student is not exists with given code: "+studentCode));

        Teacher teacher = teacherRepository.findByAccount_Code(instructorCode)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Teacher is not exists with given code: "+instructorCode));

        // get current semester:
        Semester currentSemester = semesterRepository.findByIsCurrentIsTrue();

        StudentSemester studentSemester = studentSemesterRepository.findByStudentStudentIdAndSemesterSemesterId(student.getStudentId(), currentSemester.getSemesterId());

        if(studentSemester==null)
            throw new ApiException(HttpStatus.BAD_REQUEST, "Student doesn't exists in semester: "+currentSemester.getSemesterName());

        studentSemester.setInstructor(null);

        studentSemesterRepository.save(studentSemester);

        // convert to DTO:
        return convertToStudentDto(student, currentSemester);
    }

    @Override
    public StudentDto changeInstructorOfStudent(String studentCode, String instructorCode) {

        Student student = studentRepository.findByAccount_Code(studentCode)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Student is not exists with given code: "+studentCode));

        Teacher teacher = teacherRepository.findByAccount_Code(instructorCode)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Teacher is not exists with given code: "+instructorCode));

        // get current semester:
        Semester currentSemester = semesterRepository.findByIsCurrentIsTrue();

        StudentSemester studentSemester = studentSemesterRepository.findByStudentStudentIdAndSemesterSemesterId(student.getStudentId(), currentSemester.getSemesterId());

        if(studentSemester==null)
            throw new ApiException(HttpStatus.BAD_REQUEST, "Student doesn't exists in semester: "+currentSemester.getSemesterName());

        // check if student have already registered project in this current semester -> set instructor for this project
        if(studentSemester.getProject()!=null) {
            studentSemester.getProject().setTeacher(teacher);
        }

        studentSemester.setInstructor(teacher);
        studentSemesterRepository.save(studentSemester);

        Student savedStudent = studentRepository.save(student);

        // convert to DTO:
        return convertToStudentDto(savedStudent, currentSemester);
    }

    private StudentDto convertToStudentDto(Student student,Semester currentSemester) {
        StudentDto studentDto = modelMapper.map(student.getAccount(), StudentDto.class);
        studentDto.setStudentCode(student.getAccount().getCode());
        studentDto.setStudentId(student.getStudentId());
        studentDto.setStudentClass(modelMapper.map(student.getClazz(), ClassDto.class));

        StudentSemester studentSemester = studentSemesterRepository.findByStudentStudentIdAndSemesterSemesterId(
                student.getStudentId(), currentSemester.getSemesterId()
        );
        studentDto.setSemester(
                modelMapper.map(studentSemester.getSemester(), SemesterDto.class)
        );

        studentDto.setRecommendedTeachers(
                student.getProposedTeachers().stream()
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

        return studentDto;
    }


}
