package com.quanlydoantotnghiep.DoAnTotNghiep.service;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.ObjectResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.SchoolYearDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.schoolYear.SchoolYearRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.schoolYear.SchoolYearResponse;

import java.util.List;

public interface SchoolYearService {

    List<SchoolYearDto> getAllSchoolYears();
    ObjectResponse getAllSchoolYearsByPagination(int pageNumber, int pageSize, String sortBy, String sortDir);
    SchoolYearDto createSchoolYear(SchoolYearRequest schoolYearRequest);
    String deleteSchoolYear(Long schoolYearId);
    SchoolYearDto updateSchoolYear(Long schoolYearId, SchoolYearRequest schoolYearRequest);
    SchoolYearResponse getSchoolYearById(Long schoolYearId);
}
