package com.quanlydoantotnghiep.DoAnTotNghiep.repository;

import com.quanlydoantotnghiep.DoAnTotNghiep.entity.ProgressReport;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProgressReportRepository extends JpaRepository<ProgressReport, Long> {

    @Query("""
        SELECT p FROM ProgressReport p
            WHERE p.projectStage.project.projectId = :projectId
    """)
    List<ProgressReport> findProgressReportByProject(@Param("projectId") Long projectId, Sort sort);
}
