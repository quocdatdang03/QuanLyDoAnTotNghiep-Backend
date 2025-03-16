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
    Page<Student> findByClazzFacultyFacultyId(Long facultyId, Pageable pageable);
    List<Student> findAllByAccount_CodeIn(List<String> studentCode);

    @Query("""
        SELECT s
        FROM Student s
        WHERE (LOWER(s.account.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(s.account.code) LIKE LOWER(CONCAT('%', :keyword, '%')))
          AND s.clazz.faculty.facultyId = :facultyId
    """)
    Page<Student> searchByKeywordAndFaculty(
            @Param("keyword") String keyword,
            @Param("facultyId") Long facultyId,
            Pageable pageable);

    // A IS NULL OR CONDITION : Nếu A mà có giá trị là null thì không thực hiện CONDITION
    @Query("""
        SELECT s FROM Student s
            WHERE (LOWER(s.account.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(s.account.code) LIKE LOWER(CONCAT('%', :keyword, '%')))
                AND (:classId IS NULL OR s.clazz.classId = :classId)
                AND (:facultyId IS NULL OR s.clazz.faculty.facultyId = :facultyId )
                AND (:semesterId IS NULL OR EXISTS (
                            SELECT 1 FROM s.semesters sem WHERE sem.semesterId = :semesterId
                        )
                    )
    """)
    Page<Student> findAllStudentsByKeywordAndSemesterAndFacultyAndClass(
             @Param("keyword") String keyword,
             @Param("semesterId") Long semesterId,
             @Param("facultyId") Long facultyId,
             @Param("classId") Long classId,
             Pageable pageable
    );


    @Query("""
        SELECT s FROM Student s
            WHERE (LOWER(s.account.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(s.account.code) LIKE LOWER(CONCAT('%', :keyword, '%')))
                AND (:classId IS NULL OR s.clazz.classId = :classId)
                AND (:facultyId IS NULL OR s.clazz.faculty.facultyId = :facultyId )
                AND (:semesterId IS NULL OR EXISTS (
                            SELECT 1 FROM s.semesters sem WHERE sem.semesterId = :semesterId
                        )
                    )
                AND s.instructor IS NULL
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
            WHERE (LOWER(s.account.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(s.account.code) LIKE LOWER(CONCAT('%', :keyword, '%')))
                AND (:classId IS NULL OR s.clazz.classId = :classId)
                AND (:facultyId IS NULL OR s.clazz.faculty.facultyId = :facultyId )
                AND (:semesterId IS NULL OR EXISTS (
                            SELECT 1 FROM s.semesters sem WHERE sem.semesterId = :semesterId
                        )
                    )
                AND s.instructor IS NOT NULL
                AND (:instructorCode IS NULL OR s.instructor.account.code = :instructorCode)
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
            WHERE (LOWER(s.account.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(s.account.code) LIKE LOWER(CONCAT('%', :keyword, '%')))
                AND (:classId IS NULL OR s.clazz.classId = :classId)
                AND (:semesterId IS NULL OR EXISTS (
                            SELECT 1 FROM s.semesters sem WHERE sem.semesterId = :semesterId
                        )
                    )
                AND (
                    :havingProject IS NULL OR 
                        (:havingProject = true AND s.project IS NOT NULL)
                        OR (:havingProject = false AND s.project IS NULL)
                )
                AND  s.instructor.account.code = :teacherCode
    """)
    Page<Student> findAllStudentsByInstructor(
            @Param("keyword") String keyword,
            @Param("semesterId") Long semesterId,
            @Param("classId") Long classId,
            @Param("havingProject") Boolean havingProject,
            @Param("teacherCode") String teacherCode,
            Pageable pageable
    );


//    List<Student> findByTeamTeamId(Long teamId);
}
