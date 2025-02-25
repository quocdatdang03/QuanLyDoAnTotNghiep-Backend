package com.quanlydoantotnghiep.DoAnTotNghiep.repository;

import com.quanlydoantotnghiep.DoAnTotNghiep.entity.ProgressReview;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgressReviewRepository extends JpaRepository<ProgressReview, Long> {

    List<ProgressReview> findByProjectProjectId(Long projectId, Sort sort);
}
