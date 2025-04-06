package com.quanlydoantotnghiep.DoAnTotNghiep.dto.notification;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.SemesterDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.response.TeacherAccountResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationDto {

    Long notificationId;
    String notificationTitle;
    String notificationContent;
    TeacherAccountResponse teacher;
    SemesterDto semester;
    LocalDateTime createdDate;
}
