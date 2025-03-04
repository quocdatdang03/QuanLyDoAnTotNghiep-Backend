package com.quanlydoantotnghiep.DoAnTotNghiep.controller;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.SchoolYearDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.schoolYear.SchoolYearRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.schoolYear.SchoolYearResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.AccountService;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.SchoolYearService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/schoolYears")
@RequiredArgsConstructor
public class AdminSchoolYearController {

    private final SchoolYearService schoolYearService;
    private final AccountService accountService;


    @GetMapping("/{schoolYearId}")
    public ResponseEntity<SchoolYearResponse> getSchoolYearById(
            @PathVariable("schoolYearId") Long schoolYearId
    ) {

        return ResponseEntity.ok(schoolYearService.getSchoolYearById(schoolYearId));
    }

    @PostMapping
    public ResponseEntity<SchoolYearDto> createSchoolYear(
            @RequestHeader("Authorization") String jwtToken,
            @RequestBody SchoolYearRequest schoolYearRequest
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(schoolYearService.createSchoolYear(schoolYearRequest));
    }

    @PutMapping("/{schoolYearId}")
    public ResponseEntity<SchoolYearDto> updateSchoolYear(

            @RequestBody SchoolYearRequest schoolYearRequest,   
            @PathVariable("schoolYearId") Long schoolYearId
    ) {

        return ResponseEntity.ok(schoolYearService.updateSchoolYear(schoolYearId, schoolYearRequest));
    }

    @DeleteMapping("/{schoolYearId}")
    public ResponseEntity<String> updateSchoolYear(

            @PathVariable("schoolYearId") Long schoolYearId
    ) {

        return ResponseEntity.ok(schoolYearService.deleteSchoolYear(schoolYearId));
    }

    private AccountDto getAccountDtoByJwtToken(String jwtToken) {

        String onlyToken = jwtToken.substring(7);
        AccountDto accountDto = accountService.getByJwtToken(onlyToken);

        return accountDto;
    }

}
