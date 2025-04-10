package com.quanlydoantotnghiep.DoAnTotNghiep.dto.degree;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DegreeDto {

    Long degreeId;
    String degreeName;
}
