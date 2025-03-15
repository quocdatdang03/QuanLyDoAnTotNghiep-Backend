package com.quanlydoantotnghiep.DoAnTotNghiep.dto.project.request;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProjectFileRequest {

    String nameFile;
    String pathFile;
}
