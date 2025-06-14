package com.quanlydoantotnghiep.DoAnTotNghiep.dto.project;


import com.quanlydoantotnghiep.DoAnTotNghiep.dto.SemesterDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.StudentDto;
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
    String inProgressStage; // giai đoạn đang thực hiện
    int numberOfCompletedStages; // số giai đoạn đã hoàn thành
    int totalStages; // tổng số giai đoạn cần làm (đã được phân cho sinh viên làm)
    int totalCreatedStages; // tổng số giai đoạn mà GVHD đã tạo
    LocalDateTime createdAt;
}