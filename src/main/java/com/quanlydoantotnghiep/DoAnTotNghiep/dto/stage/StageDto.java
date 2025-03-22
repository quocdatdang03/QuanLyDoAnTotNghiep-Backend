package com.quanlydoantotnghiep.DoAnTotNghiep.dto.stage;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.SemesterDto;
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
public class StageDto {

    Long stageId;
    String stageName;
    String stageTitle;
    String stageContent;
    Integer stageOrder;
    LocalDateTime startDate;
    LocalDateTime endDate;
    List<StageFileDto> stageFiles;
    SemesterDto semester;
    TeacherAccountResponse teacher;
    StageStatusDto stageStatus;
}
