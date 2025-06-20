package com.quanlydoantotnghiep.DoAnTotNghiep.dto.instructor;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssignInstructorRequest {

    String instructorCode;
    List<String> studentCodes;
}
