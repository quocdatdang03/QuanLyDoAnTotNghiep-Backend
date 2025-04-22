package com.quanlydoantotnghiep.DoAnTotNghiep.dto.teacher;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEnableStatusTeacherRequest {

    String teacherCode;
    String enableStatus;
}
