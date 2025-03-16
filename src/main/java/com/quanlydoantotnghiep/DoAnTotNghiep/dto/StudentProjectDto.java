package com.quanlydoantotnghiep.DoAnTotNghiep.dto;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.project.ProjectFileDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.project.ProjectStatusDto;
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
public class StudentProjectDto {

    Long projectId;
    String projectName;
    String projectContent;
    List<ProjectFileDto> projectFiles;
    ProjectStatusDto projectStatus;
    LocalDateTime createdAt;
}

