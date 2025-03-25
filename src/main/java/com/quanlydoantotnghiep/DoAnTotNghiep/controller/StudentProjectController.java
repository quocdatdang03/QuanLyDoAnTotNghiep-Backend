package com.quanlydoantotnghiep.DoAnTotNghiep.controller;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.project.ProjectDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.project.request.CreateProjectRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.AccountService;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.InstructorLeaderService;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student/projects")
@RequiredArgsConstructor
public class StudentProjectController {

    private final ProjectService projectService;
    private final InstructorLeaderService instructorService;
    private final AccountService accountService;

    // get project by studentCode
    @GetMapping
    public ResponseEntity<?> getProjectByStudentCode(
            @RequestParam("studentCode") String studentCode
    ) {

        return ResponseEntity.ok(projectService.getProjectByStudentCode(studentCode));
    }

    @GetMapping("/instructor")
    public ResponseEntity<?> getInstructorByStudentCode(
            @RequestParam("studentCode") String studentCode
    ) {

        return ResponseEntity.ok(instructorService.getInstructorByStudentCode(studentCode));
    }

    @PostMapping("/creation")
    public ResponseEntity<ProjectDto> createProject(
        @RequestBody CreateProjectRequest createProjectRequest
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(projectService.createProject(createProjectRequest));
    }

//    @DeleteMapping("/{projectId}")
//    public ResponseEntity<String> deleteProjectById(
//            @PathVariable("projectId") Long projectId
//    ) {
//
//        return ResponseEntity.ok(projectService.deleteProjectById(projectId));
//    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectDto> updateProject(
            @RequestHeader("Authorization") String jwtToken,
            @PathVariable("projectId") Long projectId,
            @RequestBody CreateProjectRequest updateProjectRequest
    ) {

        AccountDto accountDto = getAccountDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(projectService.updateProject(projectId, accountDto, updateProjectRequest));
    }

    @DeleteMapping("/projectFile/{projectFileId}")
    public ResponseEntity<String> deleteProjectFileById(
            @PathVariable("projectFileId") Long projectFileId
    ) {

        return ResponseEntity.ok(projectService.deleteProjectFileByProjectFileId(projectFileId));
    }

    private AccountDto getAccountDtoByJwtToken(String jwtToken) {

        String onlyToken = jwtToken.substring(7);
        AccountDto accountDto = accountService.getByJwtToken(onlyToken);

        return accountDto;
    }

}
