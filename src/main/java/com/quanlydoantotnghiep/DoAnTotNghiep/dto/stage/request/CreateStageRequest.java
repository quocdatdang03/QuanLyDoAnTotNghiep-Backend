package com.quanlydoantotnghiep.DoAnTotNghiep.dto.stage.request;

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
public class CreateStageRequest {

    String stageName;
    String stageTitle;
    String stageContent;
    LocalDateTime startDate;
    LocalDateTime endDate;
    List<StageFileRequest> stageFiles;
    
}
