package com.quanlydoantotnghiep.DoAnTotNghiep.service.impl;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.DegreeDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.FacultyDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.request.TeacherAccountRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.response.TeacherAccountResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.*;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.*;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.TeacherService;
import com.quanlydoantotnghiep.DoAnTotNghiep.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

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
            throw new ApiException(HttpStatus.BAD_REQUEST, "Email is already used by another account");

        if (accountRepository.existsByCode(request.getTeacherCode()))
            throw new ApiException(HttpStatus.BAD_REQUEST, "Teacher code is already used by another account");

        if (accountRepository.existsByPhoneNumber(request.getPhoneNumber()))
            throw new ApiException(HttpStatus.BAD_REQUEST, "Phone number is already used by another account");

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
    public TeacherAccountResponse getTeacherByTeacherCode(String teacherCode) {

        Teacher teacher = teacherRepository.findByAccount_Code(teacherCode)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Teacher is not exists with given code: "+teacherCode));

        // convert to TeacherAccountResponse
        TeacherAccountResponse teacherAccountResponse = modelMapper.map(teacher.getAccount(), TeacherAccountResponse.class);
        teacherAccountResponse.setTeacherCode(teacher.getAccount().getCode());
        teacherAccountResponse.setDegree(
                modelMapper.map(teacher.getDegree(), DegreeDto.class)
        );
        teacherAccountResponse.setFaculty(
                modelMapper.map(teacher.getFaculty(), FacultyDto.class)
        );
        teacherAccountResponse.setLeader(teacher.isLeader());

        return teacherAccountResponse;
    }
}
