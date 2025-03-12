package com.quanlydoantotnghiep.DoAnTotNghiep.dto.instructor;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RemoveInstructorFromStudentRequest {

    String studentCode;
    String teacherCode;
}
