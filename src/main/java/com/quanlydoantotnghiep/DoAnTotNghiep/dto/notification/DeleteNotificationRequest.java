package com.quanlydoantotnghiep.DoAnTotNghiep.dto.notification;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeleteNotificationRequest {

    Long notificationId;
}
