package com.quanlydoantotnghiep.DoAnTotNghiep.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "THONGBAO")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maThongBao")
    Long notificationId;

    @Column(name = "tieuDeThongBao", columnDefinition = "text")
    String notificationTitle;

    @Column(name = "noiDungThongBao", columnDefinition = "text")
    String notificationContent;

    // 1 thông báo được tạo bởi 1 GVHD
    @ManyToOne
    @JoinColumn(name = "maGiangVien", referencedColumnName = "maGiangVien", nullable = false)
    Teacher teacher;

    // 1 thông báo được tạo trong 1 học kỳ nhất định
    @ManyToOne
    @JoinColumn(name = "maHocKy", referencedColumnName = "maHocKy", nullable = false)
    Semester semester;

//    // 1 thông báo được gửi cho nhiều sinh viên (được GVHD đó HD)
//    @ManyToMany
//    @JoinTable(
//            name = "THONGBAO_SINHVIEN",
//            joinColumns = @JoinColumn(name = "maThongBao"),
//            inverseJoinColumns = @JoinColumn(name = "maSinhVienHocKy")
//    )
//    List<StudentSemester> studentSemesters = new ArrayList<>();

    @Column(name = "ngayTao", updatable = false)
    LocalDateTime createdAt;

    @Column(name = "ngayCapNhat")
    LocalDateTime updatedAt;

    @Column(name = "trangThaiXoa")
    boolean flagDelete;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
