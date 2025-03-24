package com.quanlydoantotnghiep.DoAnTotNghiep.repository;

import com.quanlydoantotnghiep.DoAnTotNghiep.entity.SchoolYear;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SchoolYearRepository extends JpaRepository<SchoolYear, Long> {

    List<SchoolYear> findByFlagDeleteIsFalse(Sort sort);
    Page<SchoolYear> findByFlagDeleteIsFalse(Pageable pageable);
    boolean existsBySchoolYearName(String schoolYearName);
    SchoolYear findBySchoolYearName(String schoolYearName);
}
