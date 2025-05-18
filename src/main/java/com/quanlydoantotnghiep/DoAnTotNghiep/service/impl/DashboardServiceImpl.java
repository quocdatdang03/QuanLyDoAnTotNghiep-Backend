package com.quanlydoantotnghiep.DoAnTotNghiep.service.impl;

import com.quanlydoantotnghiep.DoAnTotNghiep.entity.Clazz;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.Semester;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.*;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final SemesterRepository semesterRepository;
    private final StudentSemesterRepository studentSemesterRepository;
    private final ClassRepository classRepository;

    @Override
    public int countAllStudents(Long facultyId, Long classId) {
        return studentRepository.countAllStudents(facultyId, classId);
    }

    @Override
    public int countAllTeachers(Long facultyId) {
        return teacherRepository.countAllTeachers(facultyId);
    }

    @Override
    public int countAllStudentSemesters(Long facultyId, Long classId, Long semesterId) {

        // get current semester
        Semester currentSemester = semesterRepository.findByIsCurrentIsTrue();

        return studentSemesterRepository.countAllStudentSemesters(
                facultyId,
                classId,
                semesterId!=null ? semesterId : currentSemester.getSemesterId()
        );
    }

    @Override
    public int countAllRegisteredProjectStudent(Long facultyId, Long classId, Long semesterId) {

        // get current semester
        Semester currentSemester = semesterRepository.findByIsCurrentIsTrue();

        return studentSemesterRepository.countAllRegisteredProjectStudent(
                facultyId,
                classId,
                semesterId!=null ? semesterId : currentSemester.getSemesterId()
        );
    }
}
