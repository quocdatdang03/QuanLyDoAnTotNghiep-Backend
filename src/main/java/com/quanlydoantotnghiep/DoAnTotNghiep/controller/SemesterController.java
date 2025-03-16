package com.quanlydoantotnghiep.DoAnTotNghiep.controller;


import com.quanlydoantotnghiep.DoAnTotNghiep.service.SemesterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/semesters")
@RequiredArgsConstructor
public class SemesterController {

    private final SemesterService semesterService;

    @GetMapping
    public ResponseEntity<?> getAllSemesters() {

        return ResponseEntity.ok(semesterService.getAllSemesters());
    }
}
