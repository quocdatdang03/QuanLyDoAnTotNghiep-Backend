package com.quanlydoantotnghiep.DoAnTotNghiep.controller;

import com.quanlydoantotnghiep.DoAnTotNghiep.constant.AppConstant;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.instructor.RecommendTeacherRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.AccountService;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.RecommendTeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student/recommendedTeachers")
@RequiredArgsConstructor
public class StudentRecommendTeacherController {

    private final RecommendTeacherService recommendTeacherService;
    private final AccountService accountService;

    @GetMapping("/teachers")
    public ResponseEntity<?> getAllTeachersByFacultyOfStudent(
            @RequestHeader("Authorization") String jwtToken,
            @RequestParam(name = "pageNumber", required = false, defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) int pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = AppConstant.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(name = "sortBy", required = false, defaultValue = AppConstant.TEACHER_DEFAULT_SORT_BY) String sortBy,
            @RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir,
            @RequestParam(name = "sortDir", required = false, defaultValue = "") String keyword
    ) {

        AccountDto accountDto = getAccountDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(recommendTeacherService.getAllTeachersByFacultyOfStudent(accountDto, keyword, pageNumber, pageSize, sortBy, sortDir));
    }

    @GetMapping
    public ResponseEntity<?> getAllRecommendedTeacherOfStudent(
            @RequestParam("studentCode") String studentCode
    ) {

        return ResponseEntity.ok(recommendTeacherService.getAllRecommendedTeacherOfStudent(studentCode));
    }

    @PostMapping
    public ResponseEntity<?> addRecommendedTeacher(
            @RequestBody RecommendTeacherRequest recommendTeacherRequest
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(recommendTeacherService.recommendTeacher(recommendTeacherRequest));
    }

    @DeleteMapping
    public ResponseEntity<?> removeRecommendedTeacher(
            @RequestBody RecommendTeacherRequest recommendTeacherRequest
    ) {

        return ResponseEntity
                .ok(recommendTeacherService.removeRecommendTeacher(recommendTeacherRequest));
    }

    private AccountDto getAccountDtoByJwtToken(String jwtToken) {

        String onlyToken = jwtToken.substring(7);
        AccountDto accountDto = accountService.getByJwtToken(onlyToken);

        return accountDto;
    }

}
