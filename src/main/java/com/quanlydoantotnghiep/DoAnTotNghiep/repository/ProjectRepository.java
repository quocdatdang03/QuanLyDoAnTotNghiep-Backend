package com.quanlydoantotnghiep.DoAnTotNghiep.repository;

import com.quanlydoantotnghiep.DoAnTotNghiep.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

//    boolean existsByProjectName(String projectName);

    @Query("""
        SELECT p FROM Project p
            WHERE p.studentSemester.semester.semesterId = :semesterId AND p.studentSemester.student.account.code = :studentCode
    """)
    Optional<Project> findProjectByStudentAndSemester(@Param("studentCode") String studentCode, @Param("semesterId") Long semesterId);

//    @Query("""
//        SELECT p FROM Project p
//            WHERE (LOWER(p.student.account.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
//                   OR LOWER(p.student.account.code) LIKE LOWER(CONCAT('%', :keyword, '%'))
//                   OR LOWER(p.projectName) LIKE LOWER(CONCAT('%', :keyword, '%'))
//                   )
//                AND (:semesterId IS NULL OR p.semester.semesterId = :semesterId)
//                AND (:classId IS NULL OR p.student.clazz.classId = :classId)
//                AND p.teacher.account.code = :instructorCode
//    """)
//    Page<Project> findAllProjectsByInstructor(
//            @Param("keyword") String keyword,
//            @Param("semesterId") Long semesterId,
//            @Param("classId") Long classId,
//            @Param("instructorCode") String instructorCode,
//            Pageable pageable
//    );
//
//    @Query("""
//        SELECT p FROM Project p
//            WHERE p.teacher.account.code = :teacherCode AND p.semester.semesterId = :semesterId
//    """)
//    List<Project> findAllProjectsByTeacherAndSemester(
//            @Param("teacherCode") String teacherCode,
//            @Param("semesterId") Long semesterId
//    );

}
