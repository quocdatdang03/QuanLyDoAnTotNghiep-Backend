package com.quanlydoantotnghiep.DoAnTotNghiep.service;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.ObjectResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.StudentDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.request.StudentAccountRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.response.StudentAccountResponse;

public interface StudentService {

    StudentDto getStudentByStudentCode(String studentCode);
    StudentAccountResponse createAccountStudent(StudentAccountRequest request);
//    ObjectResponse getAllStudents(int pageNumber, int pageSize, String sortBy, String sortDir);
//    ObjectResponse getAllStudentsByFacultyId(Long facultyId ,int pageNumber, int pageSize, String sortBy, String sortDir);
//    ObjectResponse searchStudentsByKeywordAndFacultyId(String keyword, Long facultyId, int pageNumber, int pageSize, String sortBy, String sortDir);

    ObjectResponse filterAllStudents(String keyword, Long classId, Long facultyId, Long semesterId, int pageNumber, int pageSize, String sortBy, String sortDir);
}
