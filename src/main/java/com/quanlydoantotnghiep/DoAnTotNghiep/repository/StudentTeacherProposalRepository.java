package com.quanlydoantotnghiep.DoAnTotNghiep.repository;

import com.quanlydoantotnghiep.DoAnTotNghiep.entity.StudentTeacherProposal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentTeacherProposalRepository extends JpaRepository<StudentTeacherProposal, Long> {

    List<StudentTeacherProposal> findByStudentStudentIdAndSemesterSemesterId(Long studentId, Long semesterId);
    Optional<StudentTeacherProposal> findByStudentStudentIdAndTeacherTeacherIdAndSemesterSemesterId(Long studentId, Long teacherId, Long semesterId);
    boolean existsByStudentStudentIdAndTeacherTeacherIdAndSemesterSemesterId(Long studentId, Long teacherId, Long semesterId);
}
