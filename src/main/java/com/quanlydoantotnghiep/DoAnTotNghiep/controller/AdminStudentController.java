package com.quanlydoantotnghiep.DoAnTotNghiep.controller;

import com.quanlydoantotnghiep.DoAnTotNghiep.constant.AppConstant;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.StudentDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.student.FilterStudentRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.student.UpdateEnableStatusStudentRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/students")
@RequiredArgsConstructor
public class AdminStudentController {

    private final StudentService studentService;

    @GetMapping("/filter")
    public ResponseEntity<?> filterAllStudents(
            @RequestParam(name = "pageNumber", required = false, defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) int pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = AppConstant.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(name = "sortBy", required = false, defaultValue = AppConstant.STUDENT_DEFAULT_SORT_BY) String sortBy,
            @RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir,
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "classId", required = false) Long classId,
            @RequestParam(name = "facultyId", required = false) Long facultyId,
            @RequestParam(name = "semesterId", required = false) Long semesterId
    ) {

        return ResponseEntity.ok(studentService.filterAllStudents(
                keyword, classId, facultyId, semesterId, pageNumber, pageSize, sortBy, sortDir
        ));
    }

    // LOCK or UNLOCK Student Account
    @PatchMapping("/enableStatus")
    public ResponseEntity<StudentDto> updateEnableStatusOfStudent(
            @RequestBody UpdateEnableStatusStudentRequest request
    ) {

        return ResponseEntity.ok(studentService.updateEnableStatusOfStudent(request));
    }

}
