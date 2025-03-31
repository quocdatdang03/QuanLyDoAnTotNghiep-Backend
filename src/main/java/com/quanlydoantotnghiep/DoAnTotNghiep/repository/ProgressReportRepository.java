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
                AND (:stageId IS NULL OR p.projectStage.stage.stageId = :stageId)
    """)
    List<ProgressReport> findProgressReportByProject(
            @Param("projectId") Long projectId,
            @Param("stageId") Long stageId,
            Sort sort
    );
}
