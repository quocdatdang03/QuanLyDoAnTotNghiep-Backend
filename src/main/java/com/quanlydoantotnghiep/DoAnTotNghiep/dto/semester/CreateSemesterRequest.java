package com.quanlydoantotnghiep.DoAnTotNghiep.dto.semester;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateSemesterRequest {

    String semesterName;
    Long schoolYearId;
}
