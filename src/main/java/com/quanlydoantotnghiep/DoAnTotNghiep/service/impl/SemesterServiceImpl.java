package com.quanlydoantotnghiep.DoAnTotNghiep.service.impl;

import com.quanlydoantotnghiep.DoAnTotNghiep.constant.AppConstant;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.ObjectResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.SemesterDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.semester.CreateSemesterRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.semester.UpdateSemesterRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.SchoolYear;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.Semester;
import com.quanlydoantotnghiep.DoAnTotNghiep.exception.ApiException;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.SchoolYearRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.SemesterRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.SemesterService;
import com.quanlydoantotnghiep.DoAnTotNghiep.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SemesterServiceImpl implements SemesterService {

    private final SemesterRepository semesterRepository;
    private final SchoolYearRepository schoolYearRepository;
    private final ModelMapper modelMapper;

    @Override
    public ObjectResponse getAllSemesters(int pageNumber, int pageSize, String[] sortBy, String sortDir) {

        // Sort by schoolYearName by DESC and semesterName by DEDC
        Sort sort = Sort.by(sortBy);
        if(sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()))
            sort = sort.ascending();
        else
            sort = sort.descending();

        // paginating
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize, sort);

        Page<Semester> semesterPage = semesterRepository.findByFlagDeleteIsFalse(pageable);

        List<SemesterDto> semesterDtos = semesterPage.stream().map(
                item -> modelMapper.map(item, SemesterDto.class)
        ).collect(Collectors.toList());

        return AppUtils.createObjectResponse(semesterPage, semesterDtos);
    }

    @Override
    public SemesterDto getSemesterById(Long semesterId) {

        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Semester is not exists with given id: "+semesterId));

        return modelMapper.map(semester, SemesterDto.class);
    }

    @Transactional
    @Override
    public SemesterDto createSemester(CreateSemesterRequest createSemesterRequest) {

        SchoolYear schoolYear = schoolYearRepository.findById(createSemesterRequest.getSchoolYearId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "SchoolYear is not exists with given id: "+createSemesterRequest.getSchoolYearId()));


        // before create new semester -> update all isCurrent of each other semesters to false
        semesterRepository.updateAllIsCurrentToFalse();

        // set isCurrent of new semester to true
        Semester semester = Semester.builder()
                .semesterName(createSemesterRequest.getSemesterName())
                .schoolYear(schoolYear)
                .isCurrent(true)
                .build();

        Semester savedSemester = semesterRepository.save(semester);

        return modelMapper.map(savedSemester, SemesterDto.class);
    }

    @Transactional
    @Override
    public SemesterDto updateSemester(Long semesterId, UpdateSemesterRequest updateSemesterRequest) {

        SchoolYear schoolYear = schoolYearRepository.findById(updateSemesterRequest.getSchoolYearId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "SchoolYear is not exists with given id: "+updateSemesterRequest.getSchoolYearId()));

        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Semester is not exists with given id: "+semesterId));

        semester.setSemesterName(updateSemesterRequest.getSemesterName());
        semester.setSchoolYear(schoolYear);

        // check semester is current -> update isCurrent of all other semesters to false
        if(updateSemesterRequest.isCurrent() && !semester.isCurrent())
        {
            semesterRepository.updateAllIsCurrentToFalse();
        }

        semester.setCurrent(updateSemesterRequest.isCurrent());

        Semester savedSemester = semesterRepository.save(semester);

        return modelMapper.map(savedSemester, SemesterDto.class);
    }

    @Override
    public String deleteSemester(Long semesterId) {

        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Semester is not exists with given id: "+semesterId));

        semester.setFlagDelete(true);


        Semester savedSemester = semesterRepository.save(semester);

        return "Delete semester successfully!";
    }
}
