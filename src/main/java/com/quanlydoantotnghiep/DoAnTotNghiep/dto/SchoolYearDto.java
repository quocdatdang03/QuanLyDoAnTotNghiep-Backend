package com.quanlydoantotnghiep.DoAnTotNghiep.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SchoolYearDto {

    Long schoolYearId;
    String schoolYearName;
}
