package com.quanlydoantotnghiep.DoAnTotNghiep.service.impl;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.ClassDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.Clazz;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.ClassRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.ClassService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassServiceImpl implements ClassService {

    private final ClassRepository classRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<ClassDto> getAllClasses() {

        Sort sort = Sort.by("className").ascending();

        List<Clazz> classes = classRepository.findAll(sort);

        return classes.stream().map(
                item -> modelMapper.map(item, ClassDto.class)
        ).collect(Collectors.toList());
    }
}
