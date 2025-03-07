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

    // 1 sinh viên nhận được nhiều thông báo từ GVHD
    @ManyToMany(mappedBy = "students")
    Set<Notification> notifications = new HashSet<>();

    // 1 sinh viên có thể đề xuất nhiều GVHD
    @ManyToMany(mappedBy = "students")
    Set<Teacher> teachers = new HashSet<>();

    @Column(name = "daXoa")
    boolean flagDelete;

}
