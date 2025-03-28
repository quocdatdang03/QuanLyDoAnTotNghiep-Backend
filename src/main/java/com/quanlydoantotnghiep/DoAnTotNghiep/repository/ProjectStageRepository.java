package com.quanlydoantotnghiep.DoAnTotNghiep.repository;

import com.quanlydoantotnghiep.DoAnTotNghiep.entity.ProjectStage;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectStageRepository extends JpaRepository<ProjectStage, Long> {

    List<ProjectStage> findAllByProjectProjectId(Long projectId, Sort sort);
    Optional<ProjectStage> findByStageStageIdAndProjectProjectId(Long stageId, Long projectId);

    // Lấy ra các projectStage tiếp theo và sau đó dùng findFirst để lấy ra tk projectStage đứng ngay sau tk hiện tại
    @Query("""
        SELECT p FROM ProjectStage p
            WHERE p.project.projectId = :projectId AND p.stage.stageOrder > :currentStageOrder
                ORDER BY p.stage.stageOrder ASC
    """)
    List<ProjectStage> findNextStageOfProject(@Param("projectId") Long projectId, @Param("currentStageOrder") Integer currentStageOrder);
}
