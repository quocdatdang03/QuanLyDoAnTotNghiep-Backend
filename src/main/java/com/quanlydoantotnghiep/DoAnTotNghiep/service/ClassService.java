package com.quanlydoantotnghiep.DoAnTotNghiep.service;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.ClassDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;

import java.util.List;

public interface ClassService {

    List<ClassDto> getAllClasses();
    List<ClassDto> getAllClassesByFacultyId(Long facultyId);
}
