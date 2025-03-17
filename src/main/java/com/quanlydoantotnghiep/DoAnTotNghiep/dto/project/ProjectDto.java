package com.quanlydoantotnghiep.DoAnTotNghiep.dto.project;


import com.quanlydoantotnghiep.DoAnTotNghiep.dto.SemesterDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.StudentDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.Student;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectDto {

    Long projectId;
    String projectName;
    String projectContent;
    List<ProjectFileDto> projectFiles;
    ProjectStatusDto projectStatus;
    SemesterDto semester;
    StudentDto student;
    LocalDateTime createdAt;
}