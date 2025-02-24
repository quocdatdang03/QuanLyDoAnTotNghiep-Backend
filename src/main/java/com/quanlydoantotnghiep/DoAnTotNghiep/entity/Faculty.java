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
@Table(name = "Faculties")
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long facultyId;

    @Column(columnDefinition = "nvarchar(50)", nullable = false, unique = true)
    String facultyName;

    @OneToMany(mappedBy = "faculty")
    Set<Clazz> classes;

    @OneToMany(mappedBy = "faculty")
    Set<Teacher> teachers;

}
