package com.quanlydoantotnghiep.DoAnTotNghiep.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "SINHVIEN")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maSinhVien")
    Long studentId;

    @OneToOne
    @JoinColumn(name = "maTaiKhoan", referencedColumnName = "maTaiKhoan")
    Account account;

    @ManyToOne
    @JoinColumn(name = "maLopSH", referencedColumnName = "maLopSH")
    Clazz clazz;

//    // 1 sinh viên nhận được nhiều thông báo từ GVHD
//    @ManyToMany(mappedBy = "students")
//    Set<Notification> notifications = new HashSet<>();

    // 1 sinh viên có thể đề xuất nhiều GVHD theo tung học kỳ
    @OneToMany(mappedBy = "student")
    Set<StudentTeacherProposal> proposedTeachers = new HashSet<>();

    //  1 sinh viên chỉ có thể được quản lý bởi 1 GVHD ở 1 học kỳ (sinh viên có thể có ở nhiều học kỳ)
    @OneToMany(mappedBy = "student")
    Set<StudentSemester> studentSemesters = new HashSet<>();

    @Column(name = "daXoa")
    boolean flagDelete;

}
