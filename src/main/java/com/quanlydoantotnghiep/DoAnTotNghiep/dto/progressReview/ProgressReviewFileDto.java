package com.quanlydoantotnghiep.DoAnTotNghiep.dto.progressReview;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProgressReviewFileDto {

    Long progressReviewFileId;
    String nameFile;
    String pathFile;
}
