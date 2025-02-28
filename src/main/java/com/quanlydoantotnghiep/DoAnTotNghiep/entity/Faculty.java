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
@Table(name = "KHOA")
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maKhoa")
    Long facultyId;

    @Column(name = "tenKhoa",columnDefinition = "nvarchar(50)", nullable = false, unique = true)
    String facultyName;

    @OneToMany(mappedBy = "faculty")
    Set<Clazz> classes;

    @OneToMany(mappedBy = "faculty")
    Set<Teacher> teachers;

}
