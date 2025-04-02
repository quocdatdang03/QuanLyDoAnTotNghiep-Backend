package com.quanlydoantotnghiep.DoAnTotNghiep.controller;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReview.ProgressReviewDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReview.request.CreateProgressReviewRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReview.request.UpdateProgressReviewRequest;
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

    @GetMapping("/{progressReviewId}")
    public ResponseEntity<ProgressReviewDto> getProgressReviewById(
            @RequestHeader("Authorization") String jwtToken,
            @PathVariable("progressReviewId") Long progressReviewId
    ) {

        AccountDto accountDto = getAccountDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(progressReviewService.getProgressReviewById(progressReviewId, accountDto));
    }

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

    @PutMapping("/{progressReviewId}")
    public ResponseEntity<ProgressReviewDto> updateProgressReview(
            @RequestHeader("Authorization") String jwtToken,
            @PathVariable("progressReviewId") Long progressReviewId,
            @RequestBody UpdateProgressReviewRequest updateProgressReviewRequest
    ) {

        AccountDto accountDto = getAccountDtoByJwtToken(jwtToken);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(progressReviewService.updateProgressReview(updateProgressReviewRequest, accountDto));
    }

    @DeleteMapping("/{progressReviewId}")
    public ResponseEntity<String> deleteProgressReviewById(
            @RequestHeader("Authorization") String jwtToken,
            @PathVariable("progressReviewId") Long progressReviewId
    ) {

        AccountDto accountDto = getAccountDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(progressReviewService.deleteProgressReviewById(progressReviewId, accountDto));
    }

    @DeleteMapping("/progressReviewFile/{progressReviewFileId}")
    public ResponseEntity<String> deleteProgressReviewFileById(
            @RequestHeader("Authorization") String jwtToken,
            @PathVariable("progressReviewFileId") Long progressReviewFileId
    ) {

        AccountDto accountDto = getAccountDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(progressReviewService.deleteProgressReviewFileById(progressReviewFileId, accountDto));
    }

    private AccountDto getAccountDtoByJwtToken(String jwtToken) {

        String onlyToken = jwtToken.substring(7);
        AccountDto accountDto = accountService.getByJwtToken(onlyToken);

        return accountDto;
    }
}
