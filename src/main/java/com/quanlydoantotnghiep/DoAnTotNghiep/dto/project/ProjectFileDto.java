package com.quanlydoantotnghiep.DoAnTotNghiep.dto.project;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectFileDto {

    Long projectFileId;
    String nameFile;
    String pathFile;
}
