package com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReport;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProgressReportFileDto {

   Long progressReportFileId;
   String nameFile;
   String pathFile;
}
