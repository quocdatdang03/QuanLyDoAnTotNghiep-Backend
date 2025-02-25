package com.quanlydoantotnghiep.DoAnTotNghiep.dto.account;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountDto {

    Long accountId;
    String email;
    String password; // must delete this fields
    String code;
    String fullName;
    LocalDate dateOfBirth;
    String phoneNumber;
    boolean gender;
    String address;
    String image;
    Set<RoleDto> roles;
    boolean enable;
    Object userDetails;
}
