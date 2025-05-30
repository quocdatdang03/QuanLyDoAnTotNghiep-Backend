package com.quanlydoantotnghiep.DoAnTotNghiep.repository;

import com.quanlydoantotnghiep.DoAnTotNghiep.entity.Stage;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StageRepository extends JpaRepository<Stage, Long> {

    //COALESCE(MAX(s.stageOrder),0): nếu không có stage nào thì return 0
    @Query("""
        SELECT COALESCE(MAX(s.stageOrder),0) FROM Stage s
            WHERE s.teacher.account.code = :teacherCode AND s.semester.semesterId = :semesterId
    """)
    Integer findMaxStageOrder(
            @Param("teacherCode") String teacherCode,
            @Param("semesterId") Long semesterId
    );

    @Query("""
        SELECT s FROM Stage s
            WHERE s.teacher.teacherId = :teacherId 
                AND s.semester.semesterId = :semesterId
    """)
    List<Stage> findAllStagesByTeacherAndSemester(@Param("teacherId") Long teacherId, @Param("semesterId") Long semesterId, Sort sort);

    List<Stage> findByTeacherTeacherIdAndSemesterSemesterId(Long teacherId, Long semesterId, Sort sort);

    int countByTeacherTeacherIdAndSemesterSemesterId(Long teacherId, Long semesterId);
}
