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
@Table(name = "THONGBAO")
public class TeacherNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maThongBao")
    Long teacherNotificationId;

    @Column(name = "tieuDeThongBao", columnDefinition = "text")
    String topic;

    @Column(name = "noiDungThongBao", columnDefinition = "text")
    String content;

    @Column(name = "ngayTao")
    LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "maGiangVienHD", referencedColumnName = "maGiangVienHD")
    Teacher teacher;
}
