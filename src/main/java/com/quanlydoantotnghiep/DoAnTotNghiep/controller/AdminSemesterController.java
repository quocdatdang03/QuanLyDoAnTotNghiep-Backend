package com.quanlydoantotnghiep.DoAnTotNghiep.controller;

import com.quanlydoantotnghiep.DoAnTotNghiep.constant.AppConstant;
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
    public ResponseEntity<?> getAllSemesters(
            @RequestParam(name = "pageNumber", required = false, defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) int pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = AppConstant.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(name = "sortDir", required = false, defaultValue = "desc") String sortDir
    ) {

        String[] sortBy = {"schoolYear."+AppConstant.SCHOOLYEAR_DEFAULT_SORT_BY, AppConstant.SEMESTER_DEFAULT_SORT_BY};

        return ResponseEntity.ok(semesterService.getAllSemesters(pageNumber, pageSize, sortBy, sortDir));
    }

    @GetMapping("/{semesterId}")
    public ResponseEntity<SemesterDto> getSemesterById(
            @PathVariable("semesterId") Long semesterId
    ) {

        return ResponseEntity.ok(semesterService.getSemesterById(semesterId));
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
