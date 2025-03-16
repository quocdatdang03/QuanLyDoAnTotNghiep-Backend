package com.quanlydoantotnghiep.DoAnTotNghiep.service;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.ClassDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.ObjectResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.SemesterDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;

import java.util.List;

public interface InstructorService {

    ObjectResponse getAllStudentsOfInstructor(AccountDto accountDto, String keyword, Long semesterId, Long classId, Boolean havingProject, int pageNumber, int pageSize, String sortBy, String sortDir);
}
