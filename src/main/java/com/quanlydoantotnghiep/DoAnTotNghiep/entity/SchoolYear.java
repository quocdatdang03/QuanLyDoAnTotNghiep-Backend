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
@Table(name = "NAMHOC")
public class SchoolYear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maNamHoc")
    Long schoolYearId;

    @Column(name = "namHoc", columnDefinition = "varchar(50)", unique = true)
    String schoolYearName;

    @OneToMany(mappedBy = "schoolYear")
    Set<Semester> semesters;

    @Column(name = "trangThaiXoa")
    boolean flagDelete;
}
