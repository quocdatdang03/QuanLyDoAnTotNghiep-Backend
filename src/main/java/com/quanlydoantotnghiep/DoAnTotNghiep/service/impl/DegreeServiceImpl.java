package com.quanlydoantotnghiep.DoAnTotNghiep.service.impl;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.degree.DegreeDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.Degree;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.DegreeRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.DegreeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DegreeServiceImpl implements DegreeService {

    private final DegreeRepository degreeRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<DegreeDto> getAllDegrees() {

        // Sort by degreeName by ascending order
        Sort sort = Sort.by("degreeName").ascending();

        List<Degree> degrees = degreeRepository.findAll(sort);

        return degrees.stream()
                .map(item -> modelMapper.map(item, DegreeDto.class))
                    .collect(Collectors.toList());
    }
}
