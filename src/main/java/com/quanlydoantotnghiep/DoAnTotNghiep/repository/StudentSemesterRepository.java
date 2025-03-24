package com.quanlydoantotnghiep.DoAnTotNghiep.repository;

import com.quanlydoantotnghiep.DoAnTotNghiep.entity.StudentSemester;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentSemesterRepository extends JpaRepository<StudentSemester, Long> {

    StudentSemester findByStudentStudentIdAndSemesterSemesterId(Long studentId, Long semesterId);
}
