package com.quanlydoantotnghiep.DoAnTotNghiep.repository;

import com.quanlydoantotnghiep.DoAnTotNghiep.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByAccount_AccountId(Long accountId);
    Optional<Student> findByAccount_Code(String studentCode);
    Page<Student> findByClazzFacultyFacultyId(Long facultyId, Pageable pageable);

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

//    List<Student> findByTeamTeamId(Long teamId);
}
