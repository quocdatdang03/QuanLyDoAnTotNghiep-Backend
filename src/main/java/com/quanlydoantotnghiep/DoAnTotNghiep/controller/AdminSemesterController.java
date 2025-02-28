package com.quanlydoantotnghiep.DoAnTotNghiep.controller;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.SemesterDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.semester.CreateSemesterRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.semester.UpdateSemesterRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.SemesterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/semesters")
@RequiredArgsConstructor
public class AdminSemesterController {

    private final SemesterService semesterService;

    @GetMapping
    public ResponseEntity<?> getAllSemesters() {

        return ResponseEntity.ok(semesterService.getAllSemesters());
    }


    @PostMapping
    public ResponseEntity<SemesterDto> createSemester(
            @RequestBody CreateSemesterRequest createSemesterRequest
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(semesterService.createSemester(createSemesterRequest));
    }

    @PutMapping("/{semesterId}")
    public ResponseEntity<SemesterDto> updateSemester(
            @PathVariable("semesterId") Long semesterId,
            @RequestBody UpdateSemesterRequest updateSemesterRequest
    ) {

        return ResponseEntity.ok(semesterService.updateSemester(semesterId, updateSemesterRequest));
    }

    @DeleteMapping("/{semesterId}")
    public ResponseEntity<String> deleteSemester(
            @PathVariable("semesterId") Long semesterId
    ) {

        return ResponseEntity.ok(semesterService.deleteSemester(semesterId));
    }
}
