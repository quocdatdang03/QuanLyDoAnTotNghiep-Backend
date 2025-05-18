package com.quanlydoantotnghiep.DoAnTotNghiep.repository;

import com.quanlydoantotnghiep.DoAnTotNghiep.entity.StudentSemester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentSemesterRepository extends JpaRepository<StudentSemester, Long> {

    StudentSemester findByStudentStudentIdAndSemesterSemesterId(Long studentId, Long semesterId);

//    @Query("""
//        SELECT s FROM StudentSemester s
//            WHERE s.instructor.teacherId = :teacherId AND s.semester.semesterId = :semesterId
//    """)
//    List<StudentSemester> findAllStudentSemestersByTeacherAndSemester(@Param("teacherId") Long teacherId, @Param("semesterId") Long semesterId);

    @Query("""
        SELECT COUNT(*) FROM StudentSemester st
            WHERE st.semester.semesterId = :semesterId
                AND (:facultyId IS NULL OR st.student.clazz.faculty.facultyId = :facultyId)
                AND (:classId IS NULL OR st.student.clazz.classId = :classId)
                AND st.flagDelete = false
    """)
    int countAllStudentSemesters(
            @Param("facultyId") Long facultyId,
            @Param("classId") Long classId,
            @Param("semesterId") Long semesterId
    );

    @Query("""
        SELECT COUNT(*) FROM StudentSemester st
            WHERE st.semester.semesterId = :semesterId
                AND (:facultyId IS NULL OR st.student.clazz.faculty.facultyId = :facultyId)
                AND (:classId IS NULL OR st.student.clazz.classId = :classId)
                AND st.flagDelete = false
                AND st.project IS NOT NULL
    """)
    int countAllRegisteredProjectStudent(
            @Param("facultyId") Long facultyId,
            @Param("classId") Long classId,
            @Param("semesterId") Long semesterId
    );
}
