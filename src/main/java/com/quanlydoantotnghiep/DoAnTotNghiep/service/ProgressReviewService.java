package com.quanlydoantotnghiep.DoAnTotNghiep.service;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReview.ProgressReviewDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReview.request.CreateProgressReviewRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReview.request.UpdateProgressReviewRequest;

public interface ProgressReviewService {

    ProgressReviewDto createProgressReview(CreateProgressReviewRequest createProgressReviewRequest, AccountDto accountDto);
    ProgressReviewDto updateProgressReview(UpdateProgressReviewRequest updateProgressReviewRequest, AccountDto accountDto);
    String deleteProgressReviewFileById(Long progressReviewFileId, AccountDto accountDto);
    String deleteProgressReviewById(Long progressReviewById, AccountDto accountDto);
    ProgressReviewDto getProgressReviewById(Long progressReviewId, AccountDto accountDto);
}
