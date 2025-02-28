package com.quanlydoantotnghiep.DoAnTotNghiep.service;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.SchoolYearDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.schoolYear.SchoolYearRequest;

import java.util.List;

public interface SchoolYearService {

    List<SchoolYearDto> getAllSchoolYears();
    SchoolYearDto createSchoolYear(SchoolYearRequest schoolYearRequest);
    String deleteSchoolYear(Long schoolYearId);
    SchoolYearDto updateSchoolYear(Long schoolYearId, SchoolYearRequest schoolYearRequest);
}
