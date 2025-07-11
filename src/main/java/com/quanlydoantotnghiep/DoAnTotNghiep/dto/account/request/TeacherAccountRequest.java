package com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.request;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TeacherAccountRequest {

    String teacherCode;
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
    Long degreeId;
    Long facultyId;

    @JsonProperty("isLeader")
    boolean isLeader;
    boolean enable;
}
