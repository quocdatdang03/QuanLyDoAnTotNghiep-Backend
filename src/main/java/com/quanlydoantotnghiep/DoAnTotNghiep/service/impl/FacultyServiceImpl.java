package com.quanlydoantotnghiep.DoAnTotNghiep.service.impl;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.FacultyDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.Faculty;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.FacultyRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.FacultyService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository facultyRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<FacultyDto> getAllFaculties() {

        Sort sort = Sort.by("facultyName").ascending();

        List<Faculty> faculties = facultyRepository.findAll(sort);

        return faculties.stream().map(
                    (item) -> modelMapper.map(item, FacultyDto.class))
                    .collect(Collectors.toList()
                );
    }
}
