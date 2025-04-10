package com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.response;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.DegreeDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.FacultyDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.RoleDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class TeacherAccountResponse {

    Long accountId;
    Long teacherId;
    String teacherCode;
    String email;
    String password;
    String fullName;
    LocalDate dateOfBirth;
    String phoneNumber;
    boolean gender;
    String address;
    String image;
    Set<RoleDto> roles;
    boolean isLeader;
    boolean enable;
    DegreeDto degree;
    FacultyDto faculty;
}
