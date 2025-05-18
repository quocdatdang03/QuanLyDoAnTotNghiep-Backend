package com.quanlydoantotnghiep.DoAnTotNghiep.controller;

import com.quanlydoantotnghiep.DoAnTotNghiep.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/students")
    public ResponseEntity<Integer> countAllStudents(
            @RequestParam(name = "facultyId", required = false) Long facultyId,
            @RequestParam(name = "classId", required = false) Long classId
    ) {

        return ResponseEntity.ok(dashboardService.countAllStudents(facultyId, classId));
    }

    @GetMapping("/teachers")
    public ResponseEntity<Integer> countAllTeachers(
            @RequestParam(name = "facultyId", required = false) Long facultyId
    ) {

        return ResponseEntity.ok(dashboardService.countAllTeachers(facultyId));
    }

    @GetMapping("/studentSemesters")
    public ResponseEntity<Integer> countAllStudentSemesters(
            @RequestParam(name = "semesterId", required = false) Long semesterId,
            @RequestParam(name = "facultyId", required = false) Long facultyId,
            @RequestParam(name = "classId", required = false) Long classId
    ) {

        return ResponseEntity.ok(dashboardService.countAllStudentSemesters(facultyId, classId, semesterId));
    }

    @GetMapping("/registeredProjectStudent")
    public ResponseEntity<Integer> countAllRegisteredProjectStudent(
            @RequestParam(name = "semesterId", required = false) Long semesterId,
            @RequestParam(name = "facultyId", required = false) Long facultyId,
            @RequestParam(name = "classId", required = false) Long classId
    ) {

        return ResponseEntity.ok(dashboardService.countAllRegisteredProjectStudent(facultyId, classId, semesterId));
    }
}
