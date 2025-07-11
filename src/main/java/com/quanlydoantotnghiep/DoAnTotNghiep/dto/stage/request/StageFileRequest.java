package com.quanlydoantotnghiep.DoAnTotNghiep.dto.stage.request;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StageFileRequest {

    String nameFile;
    String pathFile;
}
