package com.quanlydoantotnghiep.DoAnTotNghiep.dto.stage.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateStageOrderRequest {

    List<Long> newStageIds;
}
