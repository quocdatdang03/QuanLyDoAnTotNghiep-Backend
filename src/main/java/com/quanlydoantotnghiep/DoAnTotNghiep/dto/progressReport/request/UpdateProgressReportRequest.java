package com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReport.request;


import com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReport.ProgressReportFileDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateProgressReportRequest {

    Long progressReportId;
    String progressReportTitle;
    String progressReportContent;
    List<ProgressReportFileDto> progressReportFiles;
}
