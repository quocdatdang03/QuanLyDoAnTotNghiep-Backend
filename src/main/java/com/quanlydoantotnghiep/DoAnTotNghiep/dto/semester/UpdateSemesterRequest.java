package com.quanlydoantotnghiep.DoAnTotNghiep.dto.semester;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateSemesterRequest {

    String semesterName;
    Long schoolYearId;

    @JsonProperty("isCurrent")
    boolean isCurrent;
}
