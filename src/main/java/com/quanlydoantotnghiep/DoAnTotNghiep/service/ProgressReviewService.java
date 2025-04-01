package com.quanlydoantotnghiep.DoAnTotNghiep.service;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReview.ProgressReviewDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReview.request.CreateProgressReviewRequest;

public interface ProgressReviewService {

    ProgressReviewDto createProgressReview(CreateProgressReviewRequest createProgressReviewRequest, AccountDto accountDto);
    String deleteProgressReviewFileById(Long progressReviewFileId, AccountDto accountDto);
}
