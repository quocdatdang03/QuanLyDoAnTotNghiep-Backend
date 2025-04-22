package com.quanlydoantotnghiep.DoAnTotNghiep.controller;

import com.quanlydoantotnghiep.DoAnTotNghiep.constant.AppConstant;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.StudentDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.response.TeacherAccountResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.teacher.UpdateEnableStatusTeacherRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/teachers")
@RequiredArgsConstructor
public class AdminTeacherController {

    private final TeacherService teacherService;

    @GetMapping
    public ResponseEntity<?> getAllTeachers(
            @RequestParam(name = "pageNumber", required = false, defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) int pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = AppConstant.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(name = "sortBy", required = false, defaultValue = AppConstant.STUDENT_DEFAULT_SORT_BY) String sortBy,
            @RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir,
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "facultyId", required = false) Long facultyId
    ) {

        return ResponseEntity.ok(teacherService.getAllTeachers(
                keyword, facultyId, pageNumber, pageSize, sortBy, sortDir
        ));
    }

    @GetMapping("/{teacherCode}")
    public ResponseEntity<TeacherAccountResponse> getTeacherByCode(
            @PathVariable("teacherCode") String teacherCode
    ) {

        return ResponseEntity.ok(teacherService.getTeacherByTeacherCode(teacherCode));
    }

    @PatchMapping("/enableStatus")
    public ResponseEntity<TeacherAccountResponse> updateEnableStatusOfStudent(
            @RequestBody UpdateEnableStatusTeacherRequest request
    ) {

        return ResponseEntity.ok(teacherService.updateEnableStatusOfTeacher(request));
    }
}
