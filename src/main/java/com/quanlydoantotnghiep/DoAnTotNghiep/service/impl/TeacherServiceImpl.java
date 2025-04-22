package com.quanlydoantotnghiep.DoAnTotNghiep.service.impl;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.DegreeDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.FacultyDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.ObjectResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.request.TeacherAccountRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.response.TeacherAccountResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.teacher.UpdateEnableStatusTeacherRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.*;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.*;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.TeacherService;
import com.quanlydoantotnghiep.DoAnTotNghiep.exception.ApiException;
import com.quanlydoantotnghiep.DoAnTotNghiep.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final DegreeRepository degreeRepository;
    private final FacultyRepository facultyRepository;
    private final ModelMapper modelMapper;

    @Override
    public TeacherAccountResponse createAccountTeacher(TeacherAccountRequest request) {

        // save account
        Set<Role> roles = new HashSet<>(roleRepository.findAllById(request.getRoleIds()));

        if (accountRepository.existsByEmail(request.getEmail()))
            throw new ApiException(HttpStatus.BAD_REQUEST, "Email đã được sử dụng bởi tài khoản khác");

        if (accountRepository.existsByCode(request.getTeacherCode()))
            throw new ApiException(HttpStatus.BAD_REQUEST, "Mã giảng viên đã được sử dụng bởi tài khoản khác");

        if (accountRepository.existsByPhoneNumber(request.getPhoneNumber()))
            throw new ApiException(HttpStatus.BAD_REQUEST, "Số điện thoại đã được sử dụng bởi tài khoản khác");

        Account account = Account.builder()
                .code(request.getTeacherCode())
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
        Degree degree = degreeRepository.findById(request.getDegreeId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Degree is not exists with given id: "+request.getDegreeId()));

        Faculty faculty = facultyRepository.findById(request.getFacultyId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Faculty is not exists with given id: "+request.getFacultyId()));

        Teacher teacher = Teacher.builder()
                .account(savedAccount)
                .faculty(faculty)
                .isLeader(request.isLeader())
                .degree(degree).build();

        Teacher savedTeacher = teacherRepository.save(teacher);

        // convert to TeacherAccountResponse
        TeacherAccountResponse teacherAccountResponse = modelMapper.map(savedAccount, TeacherAccountResponse.class);
        teacherAccountResponse.setTeacherCode(savedAccount.getCode());
        teacherAccountResponse.setDegree(
                modelMapper.map(savedTeacher.getDegree(), DegreeDto.class)
        );
        teacherAccountResponse.setFaculty(
                modelMapper.map(savedTeacher.getFaculty(), FacultyDto.class)
        );
        teacherAccountResponse.setLeader(savedTeacher.isLeader());

        return teacherAccountResponse;
    }

    @Override
    public TeacherAccountResponse updateAccountTeacher(Long teacherId, TeacherAccountRequest request) {

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Teacher is not exists with given id: "+teacherId));

        Account account = teacher.getAccount();

        // Check email, teacherCode, phoneNumber if it is changed -> check duplicate
        if (!account.getEmail().equals(request.getEmail()) &&
                accountRepository.existsByEmail(request.getEmail())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Email đã được sử dụng bởi tài khoản khác");
        }

        if (!account.getCode().equals(request.getTeacherCode()) &&
                accountRepository.existsByCode(request.getTeacherCode())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Mã giảng viên đã được sử dụng bởi tài khoản khác");
        }

        if (!account.getPhoneNumber().equals(request.getPhoneNumber()) &&
                accountRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Số điện thoại đã được sử dụng bởi tài khoản khác");
        }

        // update another information of account
        account.setCode(request.getTeacherCode());
        account.setEmail(request.getEmail());

        // if password field has value -> change password
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            account.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        account.setFullName(request.getFullName());
        account.setDateOfBirth(request.getDateOfBirth());
        account.setPhoneNumber(request.getPhoneNumber());
        account.setGender(request.isGender());
        account.setAddress(request.getAddress());
        account.setImage(request.getImage());

        Account updatedAccount = accountRepository.save(account);

        // update teacher
        Degree degree = degreeRepository.findById(request.getDegreeId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Degree is not exists with given id: " + request.getDegreeId()));

        Faculty faculty = facultyRepository.findById(request.getFacultyId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Faculty is not exists with given id: " + request.getFacultyId()));

        teacher.setDegree(degree);
        teacher.setFaculty(faculty);
        teacher.setLeader(request.isLeader());

        Teacher updatedTeacher = teacherRepository.save(teacher);

        return convertToTeacherAccountResponse(updatedTeacher);
    }

    @Override
    public TeacherAccountResponse getTeacherByTeacherCode(String teacherCode) {

        Teacher teacher = teacherRepository.findByAccount_Code(teacherCode)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Teacher is not exists with given code: "+teacherCode));

        // convert to TeacherAccountResponse
        return convertToTeacherAccountResponse(teacher);
    }

    @Override
    public TeacherAccountResponse updateEnableStatusOfTeacher(UpdateEnableStatusTeacherRequest request) {

        Teacher teacher = teacherRepository.findByAccount_Code(request.getTeacherCode())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Teacher is not exists with given code: "+request.getTeacherCode()));

        if(request.getEnableStatus().equals("lock"))
            teacher.getAccount().setEnable(false);
        else if(request.getEnableStatus().equals("unlock"))
            teacher.getAccount().setEnable(true);

        Teacher savedTeacher = teacherRepository.save(teacher);

        // convert to TeacherAccountResponse
        return convertToTeacherAccountResponse(savedTeacher);
    }

    @Override
    public ObjectResponse getAllTeachers(String keyword, Long facultyId, int pageNumber, int pageSize, String sortBy, String sortDir) {

        // create pageable
        Pageable pageable = AppUtils.createPageable(pageNumber, pageSize, sortBy, sortDir);

        Page<Teacher> teacherPage = teacherRepository.findAllTeachers(keyword, facultyId, pageable);

        // convert to TeacherAccountResponse
        List<TeacherAccountResponse> teachers = teacherPage.getContent().stream()
                .map(item -> convertToTeacherAccountResponse(item)).collect(Collectors.toList());

        return AppUtils.createObjectResponse(teacherPage, teachers);
    }

    private TeacherAccountResponse convertToTeacherAccountResponse(Teacher item) {
        TeacherAccountResponse teacherAccountResponse = modelMapper.map(item.getAccount(), TeacherAccountResponse.class);
        teacherAccountResponse.setTeacherId(item.getTeacherId());
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
}
