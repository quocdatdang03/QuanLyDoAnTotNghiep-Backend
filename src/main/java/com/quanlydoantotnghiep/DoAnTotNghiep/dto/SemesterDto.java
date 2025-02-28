package com.quanlydoantotnghiep.DoAnTotNghiep.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SemesterDto {

    Long semesterId;
    String semesterName;
    SchoolYearDto schoolYear;
    boolean isCurrent;
}
