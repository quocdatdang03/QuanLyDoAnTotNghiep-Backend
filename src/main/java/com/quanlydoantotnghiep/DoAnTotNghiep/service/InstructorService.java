package com.quanlydoantotnghiep.DoAnTotNghiep.service;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.ClassDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.ObjectResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;

import java.util.List;

public interface InstructorService {

    ObjectResponse getAllStudents(String keyword, Long classId, AccountDto accountDto, int pageNumber, int pageSize, String sortBy, String sortDir);

    List<ClassDto> getAllClassesByFaculty(AccountDto accountDto);

}
