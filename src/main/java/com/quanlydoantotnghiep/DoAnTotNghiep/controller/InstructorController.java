package com.quanlydoantotnghiep.DoAnTotNghiep.controller;

import com.quanlydoantotnghiep.DoAnTotNghiep.constant.AppConstant;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.AccountService;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/instructors")
@RequiredArgsConstructor
public class InstructorController {

    private final AccountService accountService;
    private final InstructorService instructorService;

    @GetMapping("/students/no-instructor")
    public ResponseEntity<?> getAllStudentsWithoutInstructor(
            @RequestHeader("Authorization") String jwtToken,
            @RequestParam(name = "pageNumber", required = false, defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) int pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = AppConstant.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(name = "sortBy", required = false, defaultValue = AppConstant.STUDENT_DEFAULT_SORT_BY) String sortBy,
            @RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir,
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "classId", required = false) Long classId
    ) {

        AccountDto accountDto = getAccountDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(instructorService.getAllStudentsWithoutInstructor(keyword, classId, accountDto, pageNumber, pageSize, sortBy, sortDir));
    }

    @GetMapping("/classes")
    public ResponseEntity<?> getAllClasses(
            @RequestHeader("Authorization") String jwtToken
    ) {

        AccountDto accountDto = getAccountDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(instructorService.getAllClassesByFaculty(accountDto));
    }

    // get all instructors faculty
    @GetMapping("/faculty")
    public ResponseEntity<?> getAllInstructorsByFaculty(
            @RequestHeader("Authorization") String jwtToken
    ) {

        AccountDto accountDto = getAccountDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(instructorService.getAllTeachersByFaculty(accountDto));
    }


    private AccountDto getAccountDtoByJwtToken(String jwtToken) {

        String onlyToken = jwtToken.substring(7);
        AccountDto accountDto = accountService.getByJwtToken(onlyToken);

        return accountDto;
    }

}
