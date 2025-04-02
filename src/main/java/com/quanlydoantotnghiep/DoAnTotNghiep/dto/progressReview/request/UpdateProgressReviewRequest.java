package com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReview.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReview.ProgressReviewFileDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateProgressReviewRequest {

    Long progressReviewId;
    String progressReviewTitle;
    String progressReviewContent;

    @JsonProperty("isApproved")
    boolean isApproved;
    List<ProgressReviewFileDto> progressReviewFiles;
}
