package com.quanlydoantotnghiep.DoAnTotNghiep.service.impl;

import com.quanlydoantotnghiep.DoAnTotNghiep.constant.AppConstant;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.ObjectResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.SchoolYearDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.schoolYear.SchoolYearRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.schoolYear.SchoolYearResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.SchoolYear;
import com.quanlydoantotnghiep.DoAnTotNghiep.exception.ApiException;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.SchoolYearRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.SchoolYearService;
import com.quanlydoantotnghiep.DoAnTotNghiep.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

        // Sort by schoolYearName By descending
        Sort sort = Sort.by(AppConstant.SCHOOLYEAR_DEFAULT_SORT_BY).descending();

        // get all school year:
        List<SchoolYear> schoolYears = schoolYearRepository.findByFlagDeleteIsFalse(sort);

        // convert to list SchoolYearDto
        return schoolYears.stream().map((item) -> {

            return SchoolYearDto.builder()
                    .schoolYearId(item.getSchoolYearId())
                    .schoolYearName(item.getSchoolYearName())
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public ObjectResponse getAllSchoolYearsByPagination(int pageNumber, int pageSize, String sortBy, String sortDir) {

        // Sort by schoolYearName by DESC
        Pageable pageable = AppUtils.createPageable(pageNumber, pageSize, sortBy, sortDir);

        Page<SchoolYear> schoolYearPage = schoolYearRepository.findByFlagDeleteIsFalse(pageable);

        List<SchoolYearDto> schoolYearDtos = schoolYearPage.getContent().stream()
                .map(item -> modelMapper.map(item, SchoolYearDto.class))
                .collect(Collectors.toList());

        return AppUtils.createObjectResponse(schoolYearPage, schoolYearDtos);
    }

    @Override
    public SchoolYearDto createSchoolYear(SchoolYearRequest schoolYearRequest) {

        String schoolYearNameFromRequest = schoolYearRequest.getStartYear()+"-"+schoolYearRequest.getEndYear();

        //  check if schoolYearName from request already exists -> throw exception
        if(schoolYearRepository.existsBySchoolYearName(schoolYearNameFromRequest))
        {
            SchoolYear existedSchoolYear = schoolYearRepository.findBySchoolYearName(schoolYearNameFromRequest);
            // if school year is deleted before -> set flagDelete = false ELSE throw exception
            if(existedSchoolYear.isFlagDelete())
            {
                existedSchoolYear.setFlagDelete(false);
                SchoolYear savedSchoolYear = schoolYearRepository.save(existedSchoolYear);

                return SchoolYearDto.builder()
                        .schoolYearId(savedSchoolYear.getSchoolYearId())
                        .schoolYearName(savedSchoolYear.getSchoolYearName())
                        .build();
            }
            else {
                // throw exception
                throw new ApiException(HttpStatus.BAD_REQUEST, "SchoolYearName should be unique");
            }
        }

        // else do all code lines below :
        SchoolYear schoolYear = SchoolYear.builder()
                .schoolYearName(schoolYearNameFromRequest).build();

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

        String schoolYearNameFromRequest = schoolYearRequest.getStartYear()+"-"+schoolYearRequest.getEndYear();

        SchoolYear schoolYear = schoolYearRepository.findById(schoolYearId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "SchoolYear is not exists with given id: "+schoolYearId));

        //  check if schoolYearName from request already exists -> throw exception
        if(schoolYearRepository.existsBySchoolYearName(schoolYearNameFromRequest) && !schoolYear.getSchoolYearName().equals(schoolYearNameFromRequest))
        {
            throw new ApiException(HttpStatus.BAD_REQUEST, "SchoolYearName should be unique");
        }

        schoolYear.setSchoolYearName(schoolYearNameFromRequest);


        SchoolYear savedSchoolYear = schoolYearRepository.save(schoolYear);

        return SchoolYearDto.builder()
                .schoolYearId(savedSchoolYear.getSchoolYearId())
                .schoolYearName(savedSchoolYear.getSchoolYearName())
                .build();
    }

    @Override
    public SchoolYearResponse getSchoolYearById(Long schoolYearId) {

        SchoolYear schoolYear = schoolYearRepository.findById(schoolYearId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "SchoolYear is not exists with given id: "+schoolYearId));

        String schoolYearName = schoolYear.getSchoolYearName();
        String startYear = schoolYearName.substring(0, schoolYearName.indexOf("-"));
        String endYear = schoolYearName.substring(schoolYearName.indexOf("-")+1);

        return SchoolYearResponse.builder()
                .schoolYearId(schoolYear.getSchoolYearId())
                .schoolYearName(schoolYearName)
                .startYear(startYear)
                .endYear(endYear)
                .build();
    }


}
