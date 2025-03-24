package com.quanlydoantotnghiep.DoAnTotNghiep.service.impl;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.ObjectResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.SemesterDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.StudentDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.request.StudentAccountRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.clazz.ClassDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.RoleDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.response.StudentAccountResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.*;
import com.quanlydoantotnghiep.DoAnTotNghiep.exception.ApiException;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.*;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.StudentService;
import com.quanlydoantotnghiep.DoAnTotNghiep.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final AccountRepository accountRepository;
    private final StudentRepository studentRepository;
    private final RoleRepository roleRepository;
    private final ClassRepository classRepository;
    private final FacultyRepository facultyRepository;
    private final StudentSemesterRepository studentSemesterRepository;
    private final SemesterRepository semesterRepository;
//    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

//    @Override
//    public StudentDto getStudentByStudentCode(String studentCode) {
//
//        Student student = studentRepository.findByAccount_Code(studentCode)
//                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Student is not exists with given student code: "+studentCode));
//
//        Account account = accountRepository.findById(student.getAccount().getAccountId())
//                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Account is not exists with given id: "+student.getAccount().getAccountId()));
//
//        // convert to StudentDto
//
//        StudentDto studentDto = modelMapper.map(student.getAccount(), StudentDto.class);
//        studentDto.setStudentCode(student.getAccount().getCode());
//        studentDto.setStudentId(student.getStudentId());
//        studentDto.setStudentClass(modelMapper.map(student.getClazz(), ClassDto.class));
//        studentDto.setSemesters(
//                student.getSemesters().stream().map(
//                        item -> modelMapper.map(item, SemesterDto.class)
//                ).collect(Collectors.toSet())
//        );
//        studentDto.setRecommendedTeachers(
//                student.getTeachers().stream().map((item) -> {
//
//                    return RecommendedTeacherDto.builder()
//                            .teacherId(item.getTeacherId())
//                            .teacherCode(item.getAccount().getCode())
//                            .teacherName(item.getAccount().getFullName())
//                            .build();
//                }).collect(Collectors.toList())
//        );
//
//        return studentDto;
//
//    }

    @Override
    public StudentAccountResponse createAccountStudent(StudentAccountRequest request) {

        // save account
        Set<Role> roles = new HashSet<>(roleRepository.findAllById(request.getRoleIds()));

        if (accountRepository.existsByEmail(request.getEmail()))
            throw new ApiException(HttpStatus.BAD_REQUEST, "Email is already used by another account");

        if (accountRepository.existsByCode(request.getStudentCode()))
            throw new ApiException(HttpStatus.BAD_REQUEST, "Student code is already used by another account");

        if (accountRepository.existsByPhoneNumber(request.getPhoneNumber()))
            throw new ApiException(HttpStatus.BAD_REQUEST, "Phone number is already used by another account");

        Account account = Account.builder()
                .code(request.getStudentCode())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .dateOfBirth(request.getDateOfBirth())
                .phoneNumber(request.getPhoneNumber())
                .gender(request.isGender())
                .address(request.getAddress())
                .image(request.getImage())
                .resetPasswordVerificationCode(null)
                .resetPasswordVerificationCodeExpiredAt(null)
                .roles(roles)
                .enable(request.isEnable()).build();


        Account savedAccount = accountRepository.save(account);


        // save student
        Clazz clazz = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Class is not exists with given id: "+request.getClassId()));

        Student student = Student.builder()
                                .account(savedAccount)
                                .clazz(clazz)
                                    .build();

        Student savedStudent = studentRepository.save(student);

        // convert to StudentAccountResponse
        return getStudentAccountResponse(savedStudent, savedAccount);
    }

    @Override
    public ObjectResponse filterAllStudents(String keyword, Long classId, Long facultyId, Long semesterId, int pageNumber, int pageSize, String sortBy, String sortDir) {

        // get Current semester
        Semester currentSemester = semesterRepository.findByIsCurrentIsTrue();

        Pageable pageable = AppUtils.createPageable(pageNumber, pageSize, sortBy, sortDir);

        Page<Student> pageStudents = studentRepository.findAllStudentsByKeywordAndSemesterAndFacultyAndClass(keyword, semesterId!=null ? semesterId : currentSemester.getSemesterId(), facultyId, classId, pageable);

        List<StudentDto> students = pageStudents.getContent().stream()
                .map((student) -> {
                    StudentDto studentDto = modelMapper.map(student.getAccount(), StudentDto.class);
                    studentDto.setStudentCode(student.getAccount().getCode());
                    studentDto.setStudentId(student.getStudentId());
                    studentDto.setStudentClass(modelMapper.map(student.getClazz(), ClassDto.class));

                    StudentSemester studentSemester = studentSemesterRepository.findByStudentStudentIdAndSemesterSemesterId(student.getStudentId(), semesterId!=null ? semesterId : currentSemester.getSemesterId());
                    studentDto.setSemester(
                            modelMapper.map(studentSemester.getSemester(), SemesterDto.class)
                    );

                    return studentDto;
                }).collect(Collectors.toList());

        return AppUtils.createObjectResponse(pageStudents, students);
    }

    private StudentAccountResponse getStudentAccountResponse(Student student, Account account) {
        StudentAccountResponse studentAccountResponse = modelMapper.map(account, StudentAccountResponse.class);
        studentAccountResponse.setStudentCode(account.getCode());
        studentAccountResponse.setStudentId(student.getStudentId());
        studentAccountResponse.setRoles(
                account.getRoles().stream().map((item) -> {
                    return modelMapper.map(item, RoleDto.class);
                }).collect(Collectors.toSet())
        );
        studentAccountResponse.setStudentClass(
                modelMapper.map(student.getClazz(), ClassDto.class)
        );

        return studentAccountResponse;
    }

}
