package com.quanlydoantotnghiep.DoAnTotNghiep.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "SINHVIEN_HOCKY",
        uniqueConstraints = @UniqueConstraint(columnNames = {"maSinhVien", "maHocKy"}))
public class StudentSemester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maSinhVienHocKy")
    Long studentSemesterId;

    @ManyToOne
    @JoinColumn(name = "maSinhVien", referencedColumnName = "maSinhVien", nullable = false)
    Student student;

    @ManyToOne
    @JoinColumn(name = "maHocKy", referencedColumnName = "maHocKy", nullable = false)
    Semester semester;

    @ManyToOne
    @JoinColumn(name = "maGiangVien", referencedColumnName = "maGiangVien", nullable = true)
    Teacher instructor;

    // Mỗi sinh viên trong học kỳ chỉ có 1 project
    @OneToOne(mappedBy = "studentSemester", cascade = CascadeType.ALL, orphanRemoval = true)
    Project project;

//    // Mỗi sinh viên trong học kỳ sẽ nhận được nhiều thông báo từ GVHD
//    @ManyToMany(mappedBy = "studentSemesters")
//    List<Notification> notifications = new ArrayList<>();

    @Column(name = "trangThaiXoa", nullable = false)
    boolean flagDelete;
}
