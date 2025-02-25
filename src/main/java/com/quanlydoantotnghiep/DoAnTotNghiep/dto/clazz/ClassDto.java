package com.quanlydoantotnghiep.DoAnTotNghiep.dto.clazz;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.FacultyDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClassDto {

    Long classId;
    String className;
    FacultyDto faculty;
}
