
package com.quanlydoantotnghiep.DoAnTotNghiep.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "LOPSINHHOAT")
public class Clazz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maLopSinhHoat")
    Long classId;

    @Column(name = "tenLopSinhHoat",columnDefinition = "varchar(50)", nullable = false)
    String className;

    @OneToMany(mappedBy = "clazz")
    Set<Student> students;

    @ManyToOne
    @JoinColumn(name = "maKhoa", referencedColumnName = "maKhoa")
    Faculty faculty;

    @Column(name = "trangThaiXoa")
    boolean flagDelete;
}
