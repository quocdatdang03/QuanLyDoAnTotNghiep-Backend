package com.quanlydoantotnghiep.DoAnTotNghiep.service.impl;

import com.quanlydoantotnghiep.DoAnTotNghiep.constant.AppConstant;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.SemesterDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.semester.CreateSemesterRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.semester.UpdateSemesterRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.SchoolYear;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.Semester;
import com.quanlydoantotnghiep.DoAnTotNghiep.exception.ApiException;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.SchoolYearRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.SemesterRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.SemesterService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SemesterServiceImpl implements SemesterService {

    private final SemesterRepository semesterRepository;
    private final SchoolYearRepository schoolYearRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<SemesterDto> getAllSemesters() {

        // sort by schoolYear by desc then semester by desc
        Sort sort = Sort.by("schoolYear."+AppConstant.SCHOOLYEAR_DEFAULT_SORT_BY,AppConstant.SEMESTER_DEFAULT_SORT_BY).descending();

        List<Semester> semesters = semesterRepository.findByFlagDeleteIsFalse(sort);

        return semesters.stream().map(
                item -> modelMapper.map(item, SemesterDto.class)
        ).collect(Collectors.toList());
    }

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

    @Override
    public SemesterDto updateSemester(Long semesterId, UpdateSemesterRequest updateSemesterRequest) {

        SchoolYear schoolYear = schoolYearRepository.findById(updateSemesterRequest.getSchoolYearId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "SchoolYear is not exists with given id: "+updateSemesterRequest.getSchoolYearId()));

        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Semester is not exists with given id: "+semesterId));

        semester.setSemesterName(updateSemesterRequest.getSemesterName());
        semester.setSchoolYear(schoolYear);

        // check semester is current -> update isCurrent of all other semesters to false
        if(updateSemesterRequest.isCurrent())
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
