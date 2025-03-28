package com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReport.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProgressReportFileRequest {

    String nameFile;
    String pathFile;
}
