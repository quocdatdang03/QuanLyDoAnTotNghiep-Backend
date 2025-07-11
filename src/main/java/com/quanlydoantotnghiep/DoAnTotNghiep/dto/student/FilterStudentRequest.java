package com.quanlydoantotnghiep.DoAnTotNghiep.dto.student;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilterStudentRequest {

    Long semesterId;
    Long classId;
    Long facultyId;
}
