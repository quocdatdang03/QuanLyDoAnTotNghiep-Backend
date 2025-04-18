package com.quanlydoantotnghiep.DoAnTotNghiep.service;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.ObjectResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.StudentDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.request.StudentAccountRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.response.StudentAccountResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.student.UpdateEnableStatusStudentRequest;

import java.util.List;

public interface StudentService {

    StudentDto getStudentByStudentCode(String studentCode);
    StudentAccountResponse createAccountStudent(StudentAccountRequest request);
    StudentAccountResponse updateAccountStudent(Long studentId, StudentAccountRequest request);

    ObjectResponse filterAllStudents(String keyword, Long classId, Long facultyId, Long semesterId, int pageNumber, int pageSize, String sortBy, String sortDir);

    ObjectResponse getAllStudents(String keyword, Long classId, Long facultyId, int pageNumber, int pageSize, String sortBy, String sortDir);
    ObjectResponse getAllStudentsNotEnrolledInCurrentSemester(String keyword, Long classId, Long facultyId, int pageNumber, int pageSize, String sortBy, String sortDir);
    StudentDto updateEnableStatusOfStudent(UpdateEnableStatusStudentRequest request); // lock or unlock student account

    String createStudentSemesters(Long semesterId, List<String> studentCodeList);
}
