package com.quanlydoantotnghiep.DoAnTotNghiep.controller;


import com.quanlydoantotnghiep.DoAnTotNghiep.service.SchoolYearService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/schoolYears")
@RequiredArgsConstructor
public class SchoolYearController {

    private final SchoolYearService schoolYearService;

    @GetMapping
    public ResponseEntity<?> getAllSchoolYear() {

        return ResponseEntity.ok(schoolYearService.getAllSchoolYears());
    }
}
