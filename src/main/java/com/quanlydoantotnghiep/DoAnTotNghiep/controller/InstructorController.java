package com.quanlydoantotnghiep.DoAnTotNghiep.controller;

import com.quanlydoantotnghiep.DoAnTotNghiep.constant.AppConstant;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.AccountService;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.InstructorLeaderService;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.InstructorService;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/instructor")
@RequiredArgsConstructor
public class InstructorController {

    private final InstructorService instructorService;
    private final InstructorLeaderService instructorLeaderService;
    private final AccountService accountService;
    private final ProjectService projectService;

    @GetMapping("/students")
    public ResponseEntity<?> getAllStudentsByInstructor(
            @RequestHeader("Authorization") String jwtToken,
            @RequestParam(name = "pageNumber", required = false, defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) int pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = AppConstant.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(name = "sortBy", required = false, defaultValue = AppConstant.STUDENT_DEFAULT_SORT_BY) String sortBy,
            @RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir,
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "semesterId", required = false) Long semesterId,
            @RequestParam(name = "classId", required = false) Long classId,
            @RequestParam(name = "havingProject", required = false) Boolean havingProject
    ) {

        AccountDto accountDto = getAccountDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(instructorService.getAllStudentsOfInstructor(accountDto, keyword,semesterId, classId, havingProject, pageNumber, pageSize, sortBy, sortDir));
    }

    @GetMapping("/classes")
    public ResponseEntity<?> getAllClassesByFacultyOfInstructor(
            @RequestHeader("Authorization") String jwtToken
    ) {

        AccountDto accountDto = getAccountDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(instructorLeaderService.getAllClassesByFaculty(accountDto));
    }

    @GetMapping("/projects")
    public ResponseEntity<?> getAllProjectsByInstructor(
            @RequestHeader("Authorization") String jwtToken,
            @RequestParam(name = "pageNumber", required = false, defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) int pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = AppConstant.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(name = "sortBy", required = false, defaultValue = AppConstant.PROJECT_DEFAULT_SORT_BY) String sortBy,
            @RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir,
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "semesterId", required = false) Long semesterId,
            @RequestParam(name = "classId", required = false) Long classId
    ) {

        AccountDto accountDto = getAccountDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(instructorService.getAllProjectsManagedByInstructor(accountDto, keyword, semesterId, classId, pageNumber, pageSize, sortBy, sortDir));
    }

    @GetMapping("/projects/{studentCode}")
    public ResponseEntity<?> getProjectByStudentCode(
        @PathVariable("studentCode") String studentCode
    ) {

        return ResponseEntity.ok(projectService.getProjectByStudentCode(studentCode));
    }

    private AccountDto getAccountDtoByJwtToken(String jwtToken) {

        String onlyToken = jwtToken.substring(7);
        AccountDto accountDto = accountService.getByJwtToken(onlyToken);

        return accountDto;
    }
}
