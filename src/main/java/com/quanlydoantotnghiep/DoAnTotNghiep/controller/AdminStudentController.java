package com.quanlydoantotnghiep.DoAnTotNghiep.controller;

import com.quanlydoantotnghiep.DoAnTotNghiep.constant.AppConstant;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.StudentDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.student.AddStudentSemesterRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.student.FilterStudentRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.student.UpdateEnableStatusStudentRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/students")
@RequiredArgsConstructor
public class AdminStudentController {

    private final StudentService studentService;


    // START methods for managing student account
    @GetMapping
    public ResponseEntity<?> getAllStudents(
            @RequestParam(name = "pageNumber", required = false, defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) int pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = AppConstant.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(name = "sortBy", required = false, defaultValue = AppConstant.STUDENT_DEFAULT_SORT_BY) String sortBy,
            @RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir,
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "classId", required = false) Long classId,
            @RequestParam(name = "facultyId", required = false) Long facultyId
    ) {

        return ResponseEntity.ok(studentService.getAllStudents(
                keyword, classId, facultyId, pageNumber, pageSize, sortBy, sortDir
        ));
    }

    @PatchMapping("/enableStatus")
    public ResponseEntity<StudentDto> updateEnableStatusOfStudent(
            @RequestBody UpdateEnableStatusStudentRequest request
    ) {

        return ResponseEntity.ok(studentService.updateEnableStatusOfStudent(request));
    }
    // END methods for managing student account

    // START methods for managing student semester (register student)
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


    @GetMapping("/not-enrolled")
    public ResponseEntity<?> getAllStudentNotEnrolledInCurrentSemester(
            @RequestParam(name = "pageNumber", required = false, defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) int pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = AppConstant.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(name = "sortBy", required = false, defaultValue = AppConstant.STUDENT_DEFAULT_SORT_BY) String sortBy,
            @RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir,
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "classId", required = false) Long classId,
            @RequestParam(name = "facultyId", required = false) Long facultyId
    ) {

        return ResponseEntity.ok(studentService.getAllStudentsNotEnrolledInCurrentSemester(keyword, classId, facultyId, pageNumber, pageSize, sortBy, sortDir));
    }

    @PostMapping("/studentSemester")
    public ResponseEntity<?> addStudentSemesters(
            @RequestBody AddStudentSemesterRequest addStudentSemesterRequest
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(studentService.createStudentSemesters(addStudentSemesterRequest.getCurrentSemesterId(), addStudentSemesterRequest.getStudentCodeList()));
    }

    // END methods for managing student semester (register student)

}
