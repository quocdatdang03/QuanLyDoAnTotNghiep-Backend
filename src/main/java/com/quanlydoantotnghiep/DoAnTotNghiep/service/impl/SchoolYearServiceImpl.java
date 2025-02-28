package com.quanlydoantotnghiep.DoAnTotNghiep.service.impl;

import com.quanlydoantotnghiep.DoAnTotNghiep.constant.AppConstant;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.SchoolYearDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.schoolYear.SchoolYearRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.SchoolYear;
import com.quanlydoantotnghiep.DoAnTotNghiep.exception.ApiException;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.SchoolYearRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.SchoolYearService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchoolYearServiceImpl implements SchoolYearService {

    private final SchoolYearRepository schoolYearRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<SchoolYearDto> getAllSchoolYears() {

        // Sort by schoolYearName by DESC
        Sort sort = Sort.by(AppConstant.SCHOOLYEAR_DEFAULT_SORT_BY).descending();

        List<SchoolYear> schoolYears = schoolYearRepository.findByFlagDeleteIsFalse(sort);

        return schoolYears.stream()
                .map(item -> modelMapper.map(item, SchoolYearDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public SchoolYearDto createSchoolYear(SchoolYearRequest schoolYearRequest) {

        SchoolYear schoolYear = SchoolYear.builder()
                .schoolYearName(schoolYearRequest.getStartYear()+"-"+schoolYearRequest.getEndYear()).build();

        SchoolYear savedSchoolYear = schoolYearRepository.save(schoolYear);

        return SchoolYearDto.builder()
                .schoolYearId(savedSchoolYear.getSchoolYearId())
                .schoolYearName(savedSchoolYear.getSchoolYearName())
                .build();
    }

    @Override
    public String deleteSchoolYear(Long schoolYearId) {

        SchoolYear schoolYear = schoolYearRepository.findById(schoolYearId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "SchoolYear is not exists with given id: "+schoolYearId));

        schoolYear.setFlagDelete(true);


        schoolYearRepository.save(schoolYear);

        return "Delete schoolYear successfully!";
    }

    @Override
    public SchoolYearDto updateSchoolYear(Long schoolYearId, SchoolYearRequest schoolYearRequest) {

        SchoolYear schoolYear = schoolYearRepository.findById(schoolYearId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "SchoolYear is not exists with given id: "+schoolYearId));

        schoolYear.setSchoolYearName(schoolYearRequest.getStartYear()+"-"+schoolYearRequest.getEndYear());


        SchoolYear savedSchoolYear = schoolYearRepository.save(schoolYear);

        return SchoolYearDto.builder()
                .schoolYearId(savedSchoolYear.getSchoolYearId())
                .schoolYearName(savedSchoolYear.getSchoolYearName())
                .build();
    }


}
