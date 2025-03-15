package com.quanlydoantotnghiep.DoAnTotNghiep.dto.project.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateProjectRequest {

    String projectName;
    String projectContent;
    List<ProjectFileRequest> projectFiles;
    String studentCode;
}
