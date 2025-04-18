package com.quanlydoantotnghiep.DoAnTotNghiep.controller;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.request.StudentAccountRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.request.TeacherAccountRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.response.TeacherAccountResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.response.StudentAccountResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.StudentService;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/accounts")
@RequiredArgsConstructor
public class AdminAccountController {

//    private final AccountService accountService;
    private final StudentService studentService;
    private final TeacherService teacherService;

    @PostMapping("/student")
    public ResponseEntity<StudentAccountResponse> createStudentAccount(@RequestBody StudentAccountRequest studentAccountRequest) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(studentService.createAccountStudent(studentAccountRequest));
    }

    @PostMapping("/teacher")
    public ResponseEntity<TeacherAccountResponse> createTeacherAccount(@RequestBody TeacherAccountRequest teacherAccountRequest) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(teacherService.createAccountTeacher(teacherAccountRequest));
    }

    @PutMapping("/teacher/{teacherId}")
    public ResponseEntity<TeacherAccountResponse> updateTeacherAccount(
            @PathVariable Long teacherId,
            @RequestBody TeacherAccountRequest teacherAccountRequest
    ) {

        return ResponseEntity.ok(teacherService.updateAccountTeacher(teacherId, teacherAccountRequest));
    }

    @PutMapping("/student/{studentId}")
    public ResponseEntity<StudentAccountResponse> updateStudentAccount(
            @PathVariable("studentId") Long studentId,
            @RequestBody StudentAccountRequest studentAccountRequest
    ) {

        return ResponseEntity.ok(studentService.updateAccountStudent(studentId, studentAccountRequest));
    }

//    private AccountDto getAccountDtoByJwtToken(String jwtToken) {
//
//        String onlyToken = jwtToken.substring(7);
//        AccountDto accountDto = accountService.getByJwtToken(onlyToken);
//
//        return accountDto;
//    }
}
