package com.quanlydoantotnghiep.DoAnTotNghiep.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "THONGBAO")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maThongBao")
    Long notificationId;

    @Column(name = "tieuDeThongBao", columnDefinition = "text")
    String topic;

    @Column(name = "noiDungThongBao", columnDefinition = "text")
    String content;

    @Column(name = "ngayTao")
    LocalDateTime createdDate;

    // 1 thông báo được tạo bởi 1 GVHD
    @ManyToOne
    @JoinColumn(name = "maGiangVienHD", referencedColumnName = "maGiangVienHD")
    Teacher teacher;

    // 1 thông báo được gửi cho nhiều sinh viên (được GVHD đó HD)
    @ManyToMany
    @JoinTable(
            name = "THONGBAO_SINHVIEN",
            joinColumns = @JoinColumn(name = "maThongBao"),
            inverseJoinColumns = @JoinColumn(name = "maSinhVien")
    )
    Set<Student> students = new HashSet<>();
}
