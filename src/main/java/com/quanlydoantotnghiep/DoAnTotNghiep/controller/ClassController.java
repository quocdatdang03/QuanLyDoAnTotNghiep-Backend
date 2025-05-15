package com.quanlydoantotnghiep.DoAnTotNghiep.controller;

import com.quanlydoantotnghiep.DoAnTotNghiep.service.ClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor
public class ClassController {

    private final ClassService classService;

    @GetMapping
    public ResponseEntity<?> getAllClasses() {

        return ResponseEntity.ok(classService.getAllClasses());
    }

    @GetMapping("/faculty/{facultyId}")
    public ResponseEntity<?> getAllClassesByFaculty(
            @PathVariable("facultyId") Long facultyId
    ) {

        return ResponseEntity.ok(classService.getAllClassesByFacultyId(facultyId));
    }
}
