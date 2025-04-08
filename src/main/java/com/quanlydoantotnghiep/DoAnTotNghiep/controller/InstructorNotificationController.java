package com.quanlydoantotnghiep.DoAnTotNghiep.controller;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.notification.CreateNotificationRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.notification.DeleteNotificationRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.notification.NotificationDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.notification.UpdateNotificationRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

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

    @MessageMapping("/updateNotification")
    public void updateNotification(UpdateNotificationRequest updateNotificationRequest) {

        // update notification
        NotificationDto notificationDto = instructorNotificationService.updateNotification((updateNotificationRequest));

        simpMessagingTemplate.convertAndSend(
                "/topic/notification/semester."+notificationDto.getSemester().getSemesterId()+"/teacher."+notificationDto.getTeacher().getTeacherCode(),
                notificationDto
        );
    }

    @MessageMapping("/deleteNotification")
    public void deleteNotification(DeleteNotificationRequest deleteNotificationRequest) {

        // delete notification
        NotificationDto notificationDto = instructorNotificationService.deleteNotification(deleteNotificationRequest.getNotificationId());

        // Tạo headers
        Map<String, Object> headers = new HashMap<>();
        headers.put("action", "DELETE");

        MessageHeaders messageHeaders = new MessageHeaders(headers);

        simpMessagingTemplate.convertAndSend(
                "/topic/notification/semester." + notificationDto.getSemester().getSemesterId()
                        + "/teacher." + notificationDto.getTeacher().getTeacherCode(),
                notificationDto,
                messageHeaders
        );
    }
}
