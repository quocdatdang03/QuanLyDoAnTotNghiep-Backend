package com.quanlydoantotnghiep.DoAnTotNghiep.repository;

import com.quanlydoantotnghiep.DoAnTotNghiep.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    boolean existsByProjectName(String projectName);

    Optional<Project> findByStudent_Account_Code(String studentCode);

//    Page<Project> findByTeamTeacherTeacherId(Long teacherId, Pageable pageable);
}
