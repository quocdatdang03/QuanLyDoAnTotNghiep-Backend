package com.quanlydoantotnghiep.DoAnTotNghiep.service;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.ObjectResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.response.TeacherAccountResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.instructor.RecommendTeacherRequest;

import java.util.List;

public interface RecommendTeacherService {

    public ObjectResponse getAllTeachersByFacultyOfStudent(AccountDto accountDto, String keyword, int pageNumber, int pageSize, String sortBy, String sortDir);
    public List<TeacherAccountResponse> getAllRecommendedTeacherOfStudent(String studentCode);
    public TeacherAccountResponse recommendTeacher(RecommendTeacherRequest recommendTeacherRequest);
    public TeacherAccountResponse removeRecommendTeacher(RecommendTeacherRequest recommendTeacherRequest);

}
