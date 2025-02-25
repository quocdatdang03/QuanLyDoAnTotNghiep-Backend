package com.quanlydoantotnghiep.DoAnTotNghiep.repository;


import com.quanlydoantotnghiep.DoAnTotNghiep.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findByAccount_AccountId(Long accountId);
}
