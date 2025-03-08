package com.quanlydoantotnghiep.DoAnTotNghiep.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "HOCKY")
public class Semester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maHocKy")
    Long semesterId;

    @Column(name = "hocKy")
    String semesterName;

    @ManyToOne
    @JoinColumn(name = "maNamHoc", referencedColumnName = "maNamHoc")
    SchoolYear schoolYear;

    @ManyToMany
    @JoinTable(
            name = "HOCKY_SINHVIEN",
            joinColumns = @JoinColumn(name = "maHocKy"),
            inverseJoinColumns = @JoinColumn(name = "maSinhVien")
    )
    Set<Student> students;

    @Column(name = "laHocKyHienTai")
    boolean isCurrent;

    @Column(name = "daXoa")
    boolean flagDelete;

    @OneToMany(mappedBy = "semester")
    Set<Project> projects;
}
