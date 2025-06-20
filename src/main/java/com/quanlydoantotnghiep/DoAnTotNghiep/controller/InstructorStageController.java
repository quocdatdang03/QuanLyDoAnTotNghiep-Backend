package com.quanlydoantotnghiep.DoAnTotNghiep.controller;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.stage.StageDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.stage.request.CreateStageRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.stage.request.UpdateStageOrderRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.AccountService;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.StageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/instructor/stages")
@RequiredArgsConstructor
public class InstructorStageController {

    private final StageService stageService;
    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<?> getAllStagesByTeacherAndSemester(
            @RequestHeader("Authorization") String jwtToken,
            @RequestParam(value = "semesterId", required = false) Long semesterId
    ) {

        AccountDto accountDto = getAccountDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(stageService.getAllStagesByTeacherAndSemester(accountDto, semesterId));
    }

    @GetMapping("/{stageId}")
    public ResponseEntity<StageDto> getStageById(
            @PathVariable("stageId") Long stageId
    ) {

        return ResponseEntity.ok(stageService.getStageById(stageId));
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> countAllStagesByInstructor(
            @RequestParam(name = "instructorId") Long instructorId
    ) {

        return ResponseEntity.ok(stageService.countAllStagesByInstructor(instructorId));
    }

    @GetMapping("/project/{projectId}/currentStage")
    public ResponseEntity<StageDto>  getCurrentStageByProject(
            @RequestHeader("Authorization") String jwtToken,
            @PathVariable("projectId") Long projectId
    ) {

        AccountDto accountDto = getAccountDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(stageService.getCurrentStageByProject(projectId, accountDto));
    }

    @PostMapping("/creation")
    public ResponseEntity<StageDto> createStage(
            @RequestHeader("Authorization") String jwtToken,
            @RequestBody CreateStageRequest createStageRequest
    ) {

        AccountDto accountDto = getAccountDtoByJwtToken(jwtToken);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(stageService.createStage(accountDto, createStageRequest));
    }

    @PutMapping("/{stageId}")
    public ResponseEntity<StageDto> updateStage(
            @RequestHeader("Authorization") String jwtToken,
            @PathVariable("stageId") Long stageId,
            @RequestBody CreateStageRequest updateStageRequest
    ) {

        AccountDto accountDto = getAccountDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(stageService.updateStage(stageId, accountDto, updateStageRequest));
    }

    @DeleteMapping("/{stageId}")
    public ResponseEntity<String> deleteStage(
            @RequestHeader("Authorization") String jwtToken,
            @PathVariable("stageId") Long stageId
    ) {

        AccountDto accountDto = getAccountDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(stageService.deleteStage(stageId, accountDto));
    }

    @PostMapping("/project/apply")
    public ResponseEntity<String> applyAllStagesToProject(
            @RequestHeader("Authorization") String jwtToken,
            @RequestParam("semesterId") Long semesterId,
            @RequestParam("projectId") Long projectId
    ) {

        AccountDto accountDto = getAccountDtoByJwtToken(jwtToken);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(stageService.applyAllStagesToProject(accountDto, semesterId, projectId));
    }

    @DeleteMapping("/stageFile/{stageFileId}")
    public ResponseEntity<String> deleteStageFileById(
            @PathVariable("stageFileId") Long stageFileId
    ) {

        return ResponseEntity.ok(stageService.deleteStageFileById(stageFileId));
    }

    @GetMapping("/stageStatuses")
    public ResponseEntity<?> getAllStageStatuses() {

        return ResponseEntity.ok(stageService.getAllStageStatuses());
    }

    @PatchMapping("/stageStatus")
    public ResponseEntity<StageDto> updateStatusOfStage(
            @RequestParam("stageId") Long stageId,
            @RequestParam("stageStatusId") Long stageStatusId
    ) {

        return ResponseEntity.ok(stageService.updateStageStatus(stageId, stageStatusId));
    }

    @PatchMapping("/stageOrder")
    public ResponseEntity<?> updateStageOrder(
            @RequestHeader("Authorization") String jwtToken,
            @RequestBody UpdateStageOrderRequest updateStageOrderRequest
    ) {

        AccountDto accountDto = getAccountDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(stageService.updateStageOrder(accountDto, updateStageOrderRequest.getNewStageIds()));
    }

    private AccountDto getAccountDtoByJwtToken(String jwtToken) {

        String onlyToken = jwtToken.substring(7);
        AccountDto accountDto = accountService.getByJwtToken(onlyToken);

        return accountDto;
    }
}
