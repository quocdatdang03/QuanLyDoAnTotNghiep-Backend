package com.quanlydoantotnghiep.DoAnTotNghiep.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class FacultyDto {

    Long facultyId;
    String facultyName;
}
