package com.quanlydoantotnghiep.DoAnTotNghiep.controller;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.notification.CreateNotificationRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.notification.NotificationDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class InstructorNotificationController {

    private final NotificationService instructorNotificationService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/sendNotification")
    public void sendNotification(CreateNotificationRequest createNotificationRequest) {

        // send notification
        NotificationDto notificationDto = instructorNotificationService.createNotification(createNotificationRequest);

        simpMessagingTemplate
                .convertAndSend(
                        "/topic/notification/semester."+notificationDto.getSemester().getSemesterId()+"/teacher."+notificationDto.getTeacher().getTeacherCode(),
                        notificationDto
                );
    }
}
