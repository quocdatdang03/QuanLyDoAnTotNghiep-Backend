package com.quanlydoantotnghiep.DoAnTotNghiep.controller;

import com.quanlydoantotnghiep.DoAnTotNghiep.service.DegreeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/degrees")
@RequiredArgsConstructor
public class DegreeController {

    private final DegreeService degreeService;

    @GetMapping
    public ResponseEntity<?> getAllDegrees() {

        return ResponseEntity.ok(degreeService.getAllDegrees());
    }
}
