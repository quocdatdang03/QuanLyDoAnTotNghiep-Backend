package com.quanlydoantotnghiep.DoAnTotNghiep.dto;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.RoleDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.clazz.ClassDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.instructor.RecommendedTeacherDto;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDto {

    Long accountId;
    String studentCode;
    Long studentId;
    String email;
    String password;
    String fullName;
    LocalDate dateOfBirth;
    String phoneNumber;
    boolean gender;
    String address;
    String image;
    Set<RoleDto> roles;
    boolean enable;
    ClassDto studentClass;
    Set<SemesterDto> semesters;
    List<RecommendedTeacherDto> recommendedTeachers;

}
