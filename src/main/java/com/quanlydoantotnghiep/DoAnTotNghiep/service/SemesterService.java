package com.quanlydoantotnghiep.DoAnTotNghiep.service;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.SemesterDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.semester.CreateSemesterRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.semester.UpdateSemesterRequest;

import java.util.List;

public interface SemesterService {

    List<SemesterDto> getAllSemesters();
    SemesterDto createSemester(CreateSemesterRequest createSemesterRequest);
    SemesterDto updateSemester(Long semesterId, UpdateSemesterRequest updateSemesterRequest);
    String deleteSemester(Long semesterId);
}
