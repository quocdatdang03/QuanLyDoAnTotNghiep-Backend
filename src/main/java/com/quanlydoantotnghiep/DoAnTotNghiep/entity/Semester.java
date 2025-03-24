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
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "HOCKY")
public class Semester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maHocKy")
    Long semesterId;

    @Column(name = "hocKy", unique = true)
    String semesterName;

    @ManyToOne
    @JoinColumn(name = "maNamHoc", referencedColumnName = "maNamHoc")
    SchoolYear schoolYear;

    @OneToMany(mappedBy = "semester")
    Set<StudentSemester> studentSemesters = new HashSet<>();

    @Column(name = "laHocKyHienTai")
    boolean isCurrent;

    @Column(name = "daXoa")
    boolean flagDelete;

}
