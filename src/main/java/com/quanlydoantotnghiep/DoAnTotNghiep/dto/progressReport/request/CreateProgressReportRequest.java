package com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReport.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateProgressReportRequest {

    String progressReportTitle;
    String progressReportContent;
    List<ProgressReportFileRequest> progressReportFiles;
    Long stageId;
    Long projectId;
}
