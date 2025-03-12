package com.quanlydoantotnghiep.DoAnTotNghiep.dto.account;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.DegreeDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.FacultyDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TeacherAccountDto {

    Long teacherId;
    DegreeDto degree;
    FacultyDto faculty;

    @JsonProperty("isLeader")
    boolean isLeader;
}
