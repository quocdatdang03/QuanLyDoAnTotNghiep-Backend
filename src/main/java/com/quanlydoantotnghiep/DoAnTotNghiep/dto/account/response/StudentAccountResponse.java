package com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.response;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.RoleDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.clazz.ClassDto;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentAccountResponse {

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

}
