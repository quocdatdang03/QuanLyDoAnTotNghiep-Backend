package com.quanlydoantotnghiep.DoAnTotNghiep.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("isCurrent")
    boolean isCurrent;
}
