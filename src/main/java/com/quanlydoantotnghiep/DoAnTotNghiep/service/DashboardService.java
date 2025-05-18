package com.quanlydoantotnghiep.DoAnTotNghiep.service;

public interface DashboardService {

    int countAllStudents(Long facultyId, Long classId);
    int countAllTeachers(Long facultyId);
    int countAllStudentSemesters(Long facultyId, Long classId, Long semesterId);
    int countAllRegisteredProjectStudent(Long facultyId, Long classId, Long semesterId);
}
