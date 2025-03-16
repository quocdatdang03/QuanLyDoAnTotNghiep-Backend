package com.quanlydoantotnghiep.DoAnTotNghiep.repository;

import com.quanlydoantotnghiep.DoAnTotNghiep.entity.Semester;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface SemesterRepository extends JpaRepository<Semester, Long> {

    Page<Semester> findByFlagDeleteIsFalse(Pageable pageable);
    List<Semester> findByFlagDeleteIsFalse();

    @Modifying
    @Query("UPDATE Semester s SET s.isCurrent=false")
    void updateAllIsCurrentToFalse();

    // find current Semester
    Semester findByIsCurrentIsTrue();
}
