package com.quanlydoantotnghiep.DoAnTotNghiep.service;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.ObjectResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.SemesterDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.semester.CreateSemesterRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.semester.UpdateSemesterRequest;

import java.util.List;

public interface SemesterService {

    ObjectResponse getAllSemesters(int pageNumber, int pageSize, String[] sortBy, String sortDir);
    SemesterDto getSemesterById(Long semesterId);
    SemesterDto createSemester(CreateSemesterRequest createSemesterRequest);
    SemesterDto updateSemester(Long semesterId, UpdateSemesterRequest updateSemesterRequest);
    String deleteSemester(Long semesterId);
}
