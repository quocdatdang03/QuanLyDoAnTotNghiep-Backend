package com.quanlydoantotnghiep.DoAnTotNghiep.service.impl;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.DegreeDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.FacultyDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.ObjectResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.SemesterDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.response.TeacherAccountResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.notification.CreateNotificationRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.notification.NotificationDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.notification.UpdateNotificationRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.Notification;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.Semester;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.StudentSemester;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.Teacher;
import com.quanlydoantotnghiep.DoAnTotNghiep.exception.ApiException;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.NotificationRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.SemesterRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.StudentSemesterRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.TeacherRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.NotificationService;
import com.quanlydoantotnghiep.DoAnTotNghiep.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstructorNotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final SemesterRepository semesterRepository;
    private final StudentSemesterRepository studentSemesterRepository;
    private final TeacherRepository teacherRepository;
    private final ModelMapper modelMapper;

    @Override
    public NotificationDto createNotification(CreateNotificationRequest createNotificationRequest) {

        // get teacher for validation:
        Teacher teacher = teacherRepository.findByAccount_Code(createNotificationRequest.getTeacherCode())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Teacher is not exists with given code: "+createNotificationRequest.getTeacherCode()));

        // get current semester:
        Semester currentSemester = semesterRepository.findByIsCurrentIsTrue();

        // create notification
        Notification notification = Notification.builder()
                .notificationTitle(createNotificationRequest.getNotificationTitle())
                .notificationContent(createNotificationRequest.getNotificationContent())
                .teacher(teacher)
                .semester(currentSemester)
                .flagDelete(false)
                .build();

        Notification savedNotification = notificationRepository.save(notification);

//        // apply notification to all studentSemester of this teacher
//        applyNotificationToStudentSemester(savedNotification);

        // convert to NotificationDto
        return convertToNotificationDto(savedNotification);
    }

    @Override
    public NotificationDto updateNotification(UpdateNotificationRequest updateNotificationRequest) {

        Notification notification = notificationRepository.findById(updateNotificationRequest.getNotificationId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Notification is not exists with given id: "+updateNotificationRequest.getNotificationId()));

        notification.setNotificationTitle(updateNotificationRequest.getNotificationTitle());
        notification.setNotificationContent(updateNotificationRequest.getNotificationContent());

        // save notification:
        Notification savedNotification = notificationRepository.save(notification);

        // convert to NotificationDto
        return convertToNotificationDto(savedNotification);
    }

    @Override
    public NotificationDto getNotificationById(Long notificationId) {

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Notification is not exists with given id: "+notificationId));

        // convert to NotificationDto
        return convertToNotificationDto(notification);
    }

    @Override
    public NotificationDto deleteNotification(Long notificationId) {

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Notification is not exists with given id: "+notificationId));

        notification.setFlagDelete(true);

        // save notification:
        Notification savedNotification = notificationRepository.save(notification);

        // convert to NotificationDto
        return convertToNotificationDto(savedNotification);
    }

    @Override
    public ObjectResponse getAllNotificationByTeacherAndSemester(Long semesterId, String teacherCode, int pageNumber, int pageSize) {

        // Sort by createdAt by descending
        Pageable pageable = AppUtils.createPageable(pageNumber, pageSize, "createdAt", "desc");

        // get teacher for validation:
        Teacher teacher = teacherRepository.findByAccount_Code(teacherCode)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Teacher is not exists with given code: "+teacherCode));

        // get current semester:
        Semester currentSemester = semesterRepository.findByIsCurrentIsTrue();

        Page<Notification> notificationPage = notificationRepository.findByTeacherTeacherIdAndSemesterSemesterIdAndFlagDeleteIsFalse(
                teacher.getTeacherId(),
                semesterId!=null ? semesterId : currentSemester.getSemesterId(),
                pageable
        );

        // convert to notificationDto list
        List<NotificationDto> notificationDtos = notificationPage.stream().map((item) -> {

            return convertToNotificationDto(item);
        }).collect(Collectors.toList());

        return AppUtils.createObjectResponse(notificationPage, notificationDtos);
    }

    private NotificationDto convertToNotificationDto(Notification item) {
        NotificationDto notificationDto = NotificationDto.builder()
                .notificationId(item.getNotificationId())
                .notificationTitle(item.getNotificationTitle())
                .notificationContent(item.getNotificationContent())
                .semester(modelMapper.map(item.getSemester(), SemesterDto.class))
                .createdDate(item.getCreatedAt())
                .build();

        TeacherAccountResponse teacherAccountResponse = modelMapper.map(item.getTeacher().getAccount(), TeacherAccountResponse.class);
        teacherAccountResponse.setTeacherCode(item.getTeacher().getAccount().getCode());
        teacherAccountResponse.setDegree(
                modelMapper.map(item.getTeacher().getDegree(), DegreeDto.class)
        );
        teacherAccountResponse.setFaculty(
                modelMapper.map(item.getTeacher().getFaculty(), FacultyDto.class)
        );
        teacherAccountResponse.setLeader(item.getTeacher().isLeader());

        notificationDto.setTeacher(teacherAccountResponse);

        return notificationDto;
    }

    @Override
    public TeacherAccountResponse getInstructorByStudentIdInCurrentSemester(Long studentId) {

        // get current semester:
        Semester currentSemester = semesterRepository.findByIsCurrentIsTrue();

        StudentSemester studentSemester = studentSemesterRepository.findByStudentStudentIdAndSemesterSemesterId(studentId, currentSemester.getSemesterId());

        if(studentSemester==null)
            throw new ApiException(HttpStatus.BAD_REQUEST, "StudentSemester is not exists with given studentId: "+studentId+" and semesterId: "+currentSemester.getSemesterId());

        // get instructor of student in current semester:
        Teacher teacher = studentSemester.getInstructor();

        if(teacher==null)
            throw new ApiException(HttpStatus.BAD_REQUEST, "Student don't have instructor in semester: "+currentSemester.getSemesterName());

        TeacherAccountResponse teacherAccountResponse = modelMapper.map(teacher.getAccount(), TeacherAccountResponse.class);
        teacherAccountResponse.setTeacherCode(teacher.getAccount().getCode());
        teacherAccountResponse.setDegree(
                modelMapper.map(teacher.getDegree(), DegreeDto.class)
        );
        teacherAccountResponse.setFaculty(
                modelMapper.map(teacher.getFaculty(), FacultyDto.class)
        );
        teacherAccountResponse.setLeader(teacher.isLeader());

        return teacherAccountResponse;
    }


//    private void applyNotificationToStudentSemester(Notification notification) {
//
//        // get all studentSemesters:
//        List<StudentSemester> studentSemesters = studentSemesterRepository.findAllStudentSemestersByTeacherAndSemester(
//                notification.getTeacher().getTeacherId(),
//                notification.getSemester().getSemesterId()
//        );
//
//        notification.setStudentSemesters(studentSemesters);
//
//        notificationRepository.save(notification);
//    }
}
