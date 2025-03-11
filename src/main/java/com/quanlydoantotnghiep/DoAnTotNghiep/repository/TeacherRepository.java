package com.quanlydoantotnghiep.DoAnTotNghiep.repository;


import com.quanlydoantotnghiep.DoAnTotNghiep.entity.Teacher;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findByAccount_AccountId(Long accountId);
    Optional<Teacher> findByAccount_Code(String teacherCode);

    // Get All Teachers by faculty id and excluding teacher has role ADMIN (for API of TEACHER IS LEADER)
    @Query("""
        SELECT t FROM Teacher t
            JOIN t.account a 
            JOIN a.roles r
            WHERE t.faculty.facultyId = :facultyId
            AND r.roleName <> 'ADMIN'
    """)
    List<Teacher> findByFacultyFacultyId(@Param("facultyId") Long facultyId, Sort sort);

}
