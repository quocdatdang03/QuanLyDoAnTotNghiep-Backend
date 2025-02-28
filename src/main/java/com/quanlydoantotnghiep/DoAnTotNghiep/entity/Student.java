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

    @ManyToMany(mappedBy = "students")
    Set<Announcement> announcements = new HashSet<>();

    @Column(name = "daXoa")
    boolean flagDelete;

}
