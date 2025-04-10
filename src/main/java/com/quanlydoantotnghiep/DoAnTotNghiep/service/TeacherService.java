package com.quanlydoantotnghiep.DoAnTotNghiep.service;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.ObjectResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.request.TeacherAccountRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.response.TeacherAccountResponse;

import java.util.List;

public interface TeacherService {

    TeacherAccountResponse createAccountTeacher(TeacherAccountRequest request);
    TeacherAccountResponse updateAccountTeacher(Long teacherId, TeacherAccountRequest request);
    TeacherAccountResponse getTeacherByTeacherCode(String teacherCode);

    ObjectResponse getAllTeachers(String keyword, Long facultyId, int pageNumber, int pageSize, String sortBy, String sortDir);

}
