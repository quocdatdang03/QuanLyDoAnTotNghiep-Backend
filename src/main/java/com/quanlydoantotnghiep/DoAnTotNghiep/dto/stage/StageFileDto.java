package com.quanlydoantotnghiep.DoAnTotNghiep.dto.stage;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StageFileDto {

    Long stageFileId;
    String nameFile;
    String pathFile;
}
