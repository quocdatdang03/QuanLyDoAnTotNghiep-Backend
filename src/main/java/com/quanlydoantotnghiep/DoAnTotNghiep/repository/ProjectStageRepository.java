package com.quanlydoantotnghiep.DoAnTotNghiep.repository;

import com.quanlydoantotnghiep.DoAnTotNghiep.entity.ProjectStage;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.Stage;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectStageRepository extends JpaRepository<ProjectStage, Long> {

    List<ProjectStage> findAllByProjectProjectId(Long projectId, Sort sort);
    Optional<ProjectStage> findByStageStageIdAndProjectProjectId(Long stageId, Long projectId);

    // Lấy ra các projectStage tiếp theo và sau đó dùng findFirst để lấy ra tk projectStage đứng ngay sau tk hiện tại (dùng khi create/update progressReview)
    @Query("""
        SELECT p FROM ProjectStage p
            WHERE p.project.projectId = :projectId AND p.stage.stageOrder > :currentStageOrder
                ORDER BY p.stage.stageOrder ASC
    """)
    List<ProjectStage> findNextStageOfProject(@Param("projectId") Long projectId, @Param("currentStageOrder") Integer currentStageOrder);

    // Lấy ra tk latest ProjectStage của project cụ thể, sau đó dùng findFirst để lấy ra tk projectStage mới nhất (dùng khi applyStageToAllProjectsOfTeacher)
    @Query("""
        SELECT p FROM ProjectStage p
            WHERE p.project.projectId = :projectId 
                ORDER BY p.stage.stageOrder DESC
    """)
    List<ProjectStage> findLatestStageOfProject(@Param("projectId") Long projectId);

    // get ProjectStage have stageStatus = 2 ('Đang thực hiện')
    @Query("""
        SELECT p FROM ProjectStage p
            WHERE p.project.projectId = :projectId AND p.stageStatus.stageStatusId = 2
    """)
    ProjectStage findInProgressProjectStageByProjectId(@Param("projectId") Long projectId);

    // get latest ProjectStage have stageStatus = 3 ('Đã hoàn thành)'
    @Query("""
        SELECT p FROM ProjectStage p
            WHERE p.project.projectId = :projectId AND p.stageStatus.stageStatusId = 3
            ORDER BY p.stage.stageOrder DESC
    """)
    List<ProjectStage> findLatestCompletedProjectStageByProjectId(@Param("projectId") Long projectId);

    @Query("""
        SELECT COUNT (ps) FROM ProjectStage ps
            WHERE ps.project.projectId = :projectId
                AND ps.stageStatus.stageStatusId = 3
    """)
    int countAllCompletedStagesByProject(
            @Param("projectId") Long projectId
    );

    int countByProjectProjectId(Long projectId);
}
