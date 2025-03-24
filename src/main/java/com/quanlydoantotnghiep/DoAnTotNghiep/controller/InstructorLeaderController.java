//package com.quanlydoantotnghiep.DoAnTotNghiep.controller;
//
//import com.quanlydoantotnghiep.DoAnTotNghiep.constant.AppConstant;
//import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
//import com.quanlydoantotnghiep.DoAnTotNghiep.dto.instructor.AssignInstructorRequest;
//import com.quanlydoantotnghiep.DoAnTotNghiep.dto.instructor.RemoveInstructorFromStudentRequest;
//import com.quanlydoantotnghiep.DoAnTotNghiep.service.AccountService;
//import com.quanlydoantotnghiep.DoAnTotNghiep.service.InstructorLeaderService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("api/instructor-leader")
//@RequiredArgsConstructor
//public class InstructorLeaderController {
//
//    private final AccountService accountService;
//    private final InstructorLeaderService instructorLeaderService;
//
//    @GetMapping("/students/no-instructor")
//    public ResponseEntity<?> getAllStudentsWithoutInstructor(
//            @RequestHeader("Authorization") String jwtToken,
//            @RequestParam(name = "pageNumber", required = false, defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) int pageNumber,
//            @RequestParam(name = "pageSize", required = false, defaultValue = AppConstant.DEFAULT_PAGE_SIZE) int pageSize,
//            @RequestParam(name = "sortBy", required = false, defaultValue = AppConstant.STUDENT_DEFAULT_SORT_BY) String sortBy,
//            @RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir,
//            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
//            @RequestParam(name = "classId", required = false) Long classId
//    ) {
//
//        AccountDto accountDto = getAccountDtoByJwtToken(jwtToken);
//
//        return ResponseEntity.ok(instructorLeaderService.getAllStudentsWithoutInstructor(keyword, classId, accountDto, pageNumber, pageSize, sortBy, sortDir));
//    }
//
//    @GetMapping("/students/having-instructor")
//    public ResponseEntity<?> getAllStudentsHavingInstructor(
//            @RequestHeader("Authorization") String jwtToken,
//            @RequestParam(name = "pageNumber", required = false, defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) int pageNumber,
//            @RequestParam(name = "pageSize", required = false, defaultValue = AppConstant.DEFAULT_PAGE_SIZE) int pageSize,
//            @RequestParam(name = "sortBy", required = false, defaultValue = AppConstant.STUDENT_DEFAULT_SORT_BY) String sortBy,
//            @RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir,
//            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
//            @RequestParam(name = "classId", required = false) Long classId,
//            @RequestParam(name = "instructorCode", required = false) String instructorCode
//    ) {
//
//        AccountDto accountDto = getAccountDtoByJwtToken(jwtToken);
//
//        return ResponseEntity.ok(instructorLeaderService.getAllStudentsHavingInstructor(keyword, classId, instructorCode, accountDto, pageNumber, pageSize, sortBy, sortDir));
//    }
//
//    @GetMapping("/classes")
//    public ResponseEntity<?> getAllClasses(
//            @RequestHeader("Authorization") String jwtToken
//    ) {
//
//        AccountDto accountDto = getAccountDtoByJwtToken(jwtToken);
//
//        return ResponseEntity.ok(instructorLeaderService.getAllClassesByFaculty(accountDto));
//    }
//
//    // get all instructors faculty
//    @GetMapping("/faculty")
//    public ResponseEntity<?> getAllInstructorsByFaculty(
//            @RequestHeader("Authorization") String jwtToken
//    ) {
//
//        AccountDto accountDto = getAccountDtoByJwtToken(jwtToken);
//
//        return ResponseEntity.ok(instructorLeaderService.getAllTeachersByFaculty(accountDto));
//    }
//
//    // Assign Instructors for students
//    @PutMapping("/assign-students")
//    public ResponseEntity<String> assignInstructorForStudents(
//            @RequestBody AssignInstructorRequest assignInstructorRequest
//            ) {
//
//        return ResponseEntity.ok(
//                instructorLeaderService.assignInstructorForStudents(
//                        assignInstructorRequest.getInstructorCode(),
//                        assignInstructorRequest.getStudentCodes())
//        );
//    }
//
//    // Remove instructor from student:
//    @PatchMapping("/remove-instructor")
//    public ResponseEntity<?> removeInstructorFromStudent(
//            @RequestBody RemoveInstructorFromStudentRequest request
//    ) {
//
//        return ResponseEntity.ok(instructorLeaderService.removeInstructorFromStudent(request.getStudentCode(), request.getTeacherCode()));
//    }
//
//    // Change instructor of student:
//    @PatchMapping("/change-instructor")
//    public ResponseEntity<?> changeInstructorOfStudent(
//            @RequestBody RemoveInstructorFromStudentRequest request
//    ) {
//
//        return ResponseEntity.ok(instructorLeaderService.changeInstructorOfStudent(request.getStudentCode(), request.getTeacherCode()));
//    }
//
//
//
//    private AccountDto getAccountDtoByJwtToken(String jwtToken) {
//
//        String onlyToken = jwtToken.substring(7);
//        AccountDto accountDto = accountService.getByJwtToken(onlyToken);
//
//        return accountDto;
//    }
//
//}
