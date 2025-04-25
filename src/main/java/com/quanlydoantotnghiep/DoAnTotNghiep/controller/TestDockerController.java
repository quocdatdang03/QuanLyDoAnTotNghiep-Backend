package com.quanlydoantotnghiep.DoAnTotNghiep.controller;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.request.TeacherAccountRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Set;

@RestController
@RequestMapping("/create/admin/account")
@RequiredArgsConstructor
public class TestDockerController {

    private final TeacherService teacherService;

    @PostMapping
    public ResponseEntity<?> createAdminAccount() {

        TeacherAccountRequest request = new TeacherAccountRequest();
        request.setTeacherCode("999999999");
        request.setEmail("adminute@gmail.com");
        request.setPassword("dat03122003");
        request.setFullName("DAT ADMIN DOCKER");
        request.setPhoneNumber("0339268150");
        request.setGender(true);
        request.setDateOfBirth(LocalDate.now());
        request.setAddress("123 Hai Chau, HCM City");
        request.setImage("https://example.com/images/admin.jpg");
        request.setRoleIds(Set.of(3L));
        request.setDegreeId(1L);
        request.setFacultyId(1L);
        request.setEnable(true);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(teacherService.createAccountTeacher(request));
    }
}