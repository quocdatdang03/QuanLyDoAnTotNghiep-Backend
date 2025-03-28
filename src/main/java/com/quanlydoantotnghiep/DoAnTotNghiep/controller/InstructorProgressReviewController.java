package com.quanlydoantotnghiep.DoAnTotNghiep.controller;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReview.ProgressReviewDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReview.request.CreateProgressReviewRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.AccountService;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.ProgressReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/instructor/progressReviews")
@RequiredArgsConstructor
public class InstructorProgressReviewController {

    private final ProgressReviewService progressReviewService;
    private final AccountService accountService;

    @PostMapping("/creation")
    public ResponseEntity<ProgressReviewDto> createProgressReview(
            @RequestHeader("Authorization") String jwtToken,
            @RequestBody CreateProgressReviewRequest createProgressReviewRequest
    ) {

        AccountDto accountDto = getAccountDtoByJwtToken(jwtToken);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(progressReviewService.createProgressReview(createProgressReviewRequest, accountDto));
    }

    private AccountDto getAccountDtoByJwtToken(String jwtToken) {

        String onlyToken = jwtToken.substring(7);
        AccountDto accountDto = accountService.getByJwtToken(onlyToken);

        return accountDto;
    }
}
