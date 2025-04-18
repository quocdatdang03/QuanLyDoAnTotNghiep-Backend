package com.quanlydoantotnghiep.DoAnTotNghiep.dto.student;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddStudentSemesterRequest {

    List<String> studentCodeList;
    Long currentSemesterId;
}
