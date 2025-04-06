package com.quanlydoantotnghiep.DoAnTotNghiep.service;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.ObjectResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.response.TeacherAccountResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.notification.CreateNotificationRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.notification.NotificationDto;

import java.util.List;

public interface NotificationService {

//    NotificationDto createNotification(CreateNotificationRequest createNotificationRequest, AccountDto accountDto);
    NotificationDto createNotification(CreateNotificationRequest createNotificationRequest);
    ObjectResponse getAllNotificationByTeacherAndSemester(Long semesterId, String teacherCode, int pageNumber, int pageSize);

    // get instructor by studentId in current semester
    TeacherAccountResponse getInstructorByStudentIdInCurrentSemester(Long studentId);
}
