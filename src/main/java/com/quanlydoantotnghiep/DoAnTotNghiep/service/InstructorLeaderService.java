package com.quanlydoantotnghiep.DoAnTotNghiep.service;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.ObjectResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.StudentDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.response.TeacherAccountResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.clazz.ClassDto;

import java.util.List;

public interface InstructorLeaderService {

    ObjectResponse getAllStudentsWithoutInstructor(String keyword, Long classId, AccountDto accountDto, int pageNumber, int pageSize, String sortBy, String sortDir);

    ObjectResponse getAllStudentsHavingInstructor(String keyword, Long classId, String instructorCode, AccountDto accountDto, int pageNumber, int pageSize, String sortBy, String sortDir);

    ObjectResponse getAllProjects(String keyword, Long semesterId, Long classId, Long projectStatusId, String instructorCode, AccountDto accountDto, int pageNumber, int pageSize, String sortBy, String sortDir);

    TeacherAccountResponse getInstructorByStudentCode(String studentCode);

    List<ClassDto> getAllClassesByFaculty(AccountDto accountDto);

    List<TeacherAccountResponse> getAllTeachersByFaculty(AccountDto accountDto);

    String assignInstructorForStudents(String teacherCode, List<String> studentCodeList);

    StudentDto removeInstructorFromStudent(String studentCode, String instructorCode);

    StudentDto changeInstructorOfStudent(String studentCode, String instructorCode);


}
