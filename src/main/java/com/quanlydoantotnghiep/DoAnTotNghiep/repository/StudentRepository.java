package com.quanlydoantotnghiep.DoAnTotNghiep.repository;

import com.quanlydoantotnghiep.DoAnTotNghiep.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByAccount_AccountId(Long accountId);
    Optional<Student> findByAccount_Code(String studentCode);
//    Page<Student> findByClazzFacultyFacultyId(Long facultyId, Pageable pageable);
    List<Student> findAllByAccount_CodeIn(List<String> studentCode);
//
//    @Query("""
//        SELECT s
//        FROM Student s
//        WHERE (LOWER(s.account.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
//           OR LOWER(s.account.code) LIKE LOWER(CONCAT('%', :keyword, '%')))
//          AND s.clazz.faculty.facultyId = :facultyId
//    """)
//    Page<Student> searchByKeywordAndFaculty(
//            @Param("keyword") String keyword,
//            @Param("facultyId") Long facultyId,
//            Pageable pageable);

    // A IS NULL OR CONDITION : Nếu A mà có giá trị là null thì không thực hiện CONDITION
//    @Query("""
//        SELECT s FROM Student s
//            WHERE (
//                    LOWER(s.account.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
//                    OR LOWER(s.account.code) LIKE LOWER(CONCAT('%', :keyword, '%'))
//                )
//                AND (:classId IS NULL OR s.clazz.classId = :classId)
//                AND (:facultyId IS NULL OR s.clazz.faculty.facultyId = :facultyId )
//                AND EXISTS (
//                            SELECT 1 FROM s.studentSemesters stm WHERE stm.semester.semesterId = :semesterId
//                        )
//
//    """)
    @Query("""
         SELECT s FROM Student s
         JOIN s.studentSemesters stm
         WHERE (LOWER(s.account.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(s.account.code) LIKE LOWER(CONCAT('%', :keyword, '%')))
             AND (:classId IS NULL OR s.clazz.classId = :classId)
             AND (:facultyId IS NULL OR s.clazz.faculty.facultyId = :facultyId)
             AND stm.semester.semesterId = :semesterId
             and stm.flagDelete = false
    """)
    Page<Student> findAllStudentsByKeywordAndSemesterAndFacultyAndClass(
             @Param("keyword") String keyword,
             @Param("semesterId") Long semesterId,
             @Param("facultyId") Long facultyId,
             @Param("classId") Long classId,
             Pageable pageable
    );


    // CÁCH 1 : DÙNG EXISTS
//    @Query("""
//        SELECT s FROM Student s
//            WHERE (LOWER(s.account.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
//                OR LOWER(s.account.code) LIKE LOWER(CONCAT('%', :keyword, '%')))
//                AND (:classId IS NULL OR s.clazz.classId = :classId)
//                AND (:facultyId IS NULL OR s.clazz.faculty.facultyId = :facultyId )
//                AND EXISTS (
//                    SELECT 1 FROM s.studentSemesters stm
//                        WHERE stm.instructor IS NULL
//                              AND (:semesterId IS NULL OR stm.semester.semesterId = :semesterId)
//                )
//    """)
    // CÁCH  : DÙNG JOIN
    @Query("""
         SELECT s FROM Student s
         JOIN s.studentSemesters stm
         WHERE (LOWER(s.account.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(s.account.code) LIKE LOWER(CONCAT('%', :keyword, '%')))
             AND (:classId IS NULL OR s.clazz.classId = :classId)
             AND (:facultyId IS NULL OR s.clazz.faculty.facultyId = :facultyId)
             AND stm.instructor IS NULL
             AND (:semesterId IS NULL OR stm.semester.semesterId = :semesterId)
    """)
    Page<Student> findAllStudentsWithoutInstructor(
            @Param("keyword") String keyword,
            @Param("semesterId") Long semesterId,
            @Param("facultyId") Long facultyId,
            @Param("classId") Long classId,
            Pageable pageable
    );

    @Query("""
        SELECT s FROM Student s
        JOIN s.studentSemesters stm
            WHERE (LOWER(s.account.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(s.account.code) LIKE LOWER(CONCAT('%', :keyword, '%')))
                AND (:classId IS NULL OR s.clazz.classId = :classId)
                AND (:facultyId IS NULL OR s.clazz.faculty.facultyId = :facultyId )
                AND stm.instructor IS NOT NULL
                AND (:semesterId IS NULL OR stm.semester.semesterId = :semesterId)
                AND (:instructorCode IS NULL OR stm.instructor.account.code = :instructorCode)
    """)
    Page<Student> findAllStudentsHavingInstructor(
            @Param("keyword") String keyword,
            @Param("semesterId") Long semesterId,
            @Param("facultyId") Long facultyId,
            @Param("classId") Long classId,
            @Param("instructorCode") String instructorCode,
            Pageable pageable
    );

    @Query("""
        SELECT s FROM Student s
            JOIN s.studentSemesters stm
            WHERE (LOWER(s.account.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(s.account.code) LIKE LOWER(CONCAT('%', :keyword, '%')))
                AND (:classId IS NULL OR s.clazz.classId = :classId)
                AND (:semesterId IS NULL OR stm.semester.semesterId = :semesterId)
                AND (
                    :havingProject IS NULL OR
                        (
                            (:havingProject = true AND stm.project IS NOT NULL)
                            OR (:havingProject = false AND stm.project IS NULL)
                        )
                )
                AND stm.instructor.account.code = :teacherCode
    """)
    Page<Student> findAllStudentsByInstructorAndSemester(
            @Param("keyword") String keyword,
            @Param("semesterId") Long semesterId,
            @Param("classId") Long classId,
            @Param("havingProject") Boolean havingProject,
            @Param("teacherCode") String teacherCode,
            Pageable pageable
    );

    @Query("""
        SELECT s FROM Student s
            WHERE (LOWER(s.account.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) 
                    OR LOWER(s.account.code) LIKE LOWER(CONCAT('%', :keyword, '%'))
                  )
                  AND (:facultyId IS NULL OR s.clazz.faculty.facultyId = :facultyId)
                  AND (:classId IS NULL OR s.clazz.classId = :classId)
    """)
    Page<Student> findAllStudents(
            @Param("keyword") String keyword,
            @Param("facultyId") Long facultyId,
            @Param("classId") Long classId,
            Pageable pageable
    );

    @Query("""
        SELECT s FROM Student s
           WHERE (LOWER(s.account.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) 
                    OR LOWER(s.account.code) LIKE LOWER(CONCAT('%', :keyword, '%'))
                  )
                  AND NOT EXISTS (
                    SELECT 1 FROM s.studentSemesters stm WHERE stm.semester.semesterId = :currentSemesterId and stm.flagDelete = false
                  )
                  AND (:facultyId IS NULL OR s.clazz.faculty.facultyId = :facultyId)
                  AND (:classId IS NULL OR s.clazz.classId = :classId)
    """)
    Page<Student> findAllStudentsNotInCurrentSemester(
            @Param("keyword") String keyword,
            @Param("currentSemesterId") Long currentSemesterId,
            @Param("facultyId") Long facultyId,
            @Param("classId") Long classId,
            Pageable pageable
    );

    @Query("""
        SELECT COUNT(*) FROM Student s
            WHERE (:facultyId IS NULL OR s.clazz.faculty.facultyId = :facultyId)
                    AND (:classId IS NULL OR s.clazz.classId = :classId)
    """)
    int countAllStudents(
            @Param("facultyId") Long facultyId,
            @Param("classId") Long classId
    );

}
