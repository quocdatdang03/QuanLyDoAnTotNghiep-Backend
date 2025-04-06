package com.quanlydoantotnghiep.DoAnTotNghiep.controller;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.StudentDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.response.TeacherAccountResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.NotificationService;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final NotificationService notificationService;

    @GetMapping("/{studentCode}")
    public ResponseEntity<StudentDto> getStudentByStudentCode(
            @PathVariable("studentCode") String studentCode
    ) {


        return ResponseEntity.ok(studentService.getStudentByStudentCode(studentCode));
    }

    // for getting instructor for get all notifications
    @GetMapping("/{studentId}/instructor")
    public ResponseEntity<TeacherAccountResponse> getInstructorByStudentIdInCurrentSemester(
            @PathVariable("studentId") Long studentId
    ) {

        return ResponseEntity.ok(notificationService.getInstructorByStudentIdInCurrentSemester(studentId));
    }
}
