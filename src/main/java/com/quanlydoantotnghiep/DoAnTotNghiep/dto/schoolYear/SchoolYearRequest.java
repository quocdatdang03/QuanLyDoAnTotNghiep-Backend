package com.quanlydoantotnghiep.DoAnTotNghiep.dto.schoolYear;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SchoolYearRequest {

    String startYear;
    String endYear;
}
