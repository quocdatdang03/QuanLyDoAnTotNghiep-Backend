package com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReport;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.StudentDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReview.ProgressReviewDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.stage.StageDto;
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
public class ProgressReportDto {

    Long progressReportId;
    String progressReportTitle;
    String progressReportContent;
    boolean isApproved;
    List<ProgressReportFileDto> progressReportFiles;
    List<ProgressReviewDto> progressReviews;
    StageDto stage;
    LocalDateTime createdDate;
}
