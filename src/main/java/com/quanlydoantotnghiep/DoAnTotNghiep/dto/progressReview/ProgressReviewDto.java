package com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReview;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.response.TeacherAccountResponse;
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
public class ProgressReviewDto {

    Long progressReviewId;
    String progressReviewTitle;
    String progressReviewContent;
    boolean isApproved;
    List<ProgressReviewFileDto> progressReviewFiles;
    TeacherAccountResponse teacher;
    LocalDateTime createdDate;
    LocalDateTime updatedDate;
}
