package com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentAccountRequest {

    String studentCode;
    String email;
    String password;
    String fullName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    String phoneNumber;
    boolean gender;
    String address;
    String image;
    Set<Long> roleIds;
    Long classId;
    boolean enable;
}
