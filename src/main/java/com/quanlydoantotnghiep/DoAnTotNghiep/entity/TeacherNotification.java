package com.quanlydoantotnghiep.DoAnTotNghiep.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "TeacherNotifications")
public class TeacherNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long teacherNotificationId;

    @Column(columnDefinition = "text")
    String topic;

    @Column(columnDefinition = "text")
    String content;

    LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "teacherId", referencedColumnName = "teacherId")
    Teacher teacher;
}
