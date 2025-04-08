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

    @Query("""
        SELECT p FROM Project p
            WHERE (LOWER(p.studentSemester.student.account.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(p.studentSemester.student.account.code) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(p.projectName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   )
                AND (:semesterId IS NULL OR p.studentSemester.semester.semesterId = :semesterId)
                AND (:classId IS NULL OR p.studentSemester.student.clazz.classId = :classId)
                AND p.studentSemester.instructor.account.code = :instructorCode
    """)
    Page<Project> findAllProjectsByInstructorAndSemester(
            @Param("keyword") String keyword,
            @Param("semesterId") Long semesterId,
            @Param("classId") Long classId,
            @Param("instructorCode") String instructorCode,
            Pageable pageable
    );

    @Query("""
        SELECT p FROM Project p
            WHERE p.teacher.account.code = :teacherCode AND p.studentSemester.semester.semesterId = :semesterId
    """)
    List<Project> findAllProjectsByTeacherAndSemester(
            @Param("teacherCode") String teacherCode,
            @Param("semesterId") Long semesterId
    );

    @Query("""
        SELECT p FROM Project p
            WHERE (LOWER(p.studentSemester.student.account.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(p.studentSemester.student.account.code) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(p.projectName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   )
                AND (:semesterId IS NULL OR p.studentSemester.semester.semesterId = :semesterId)
                AND p.studentSemester.student.clazz.faculty.facultyId = :facultyId
                AND (:classId IS NULL OR p.studentSemester.student.clazz.classId = :classId)
                AND (:projectStatusId IS NULL OR p.projectStatus.projectStatusId = :projectStatusId)
                AND (:instructorCode IS NULL OR p.studentSemester.instructor.account.code = :instructorCode)
    """)
    Page<Project> findAllProjectsByInstructorAndSemesterAndStatus(
            @Param("keyword") String keyword,
            @Param("semesterId") Long semesterId,
            @Param("facultyId") Long facultyId,
            @Param("classId") Long classId,
            @Param("projectStatusId") Long projectStatusId,
            @Param("instructorCode") String instructorCode,
            Pageable pageable
    );

}
