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
@Table(name = "Degrees")
public class Degree {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long degreeId;

    @Column(columnDefinition = "varchar(50)", unique = true, nullable = false)
    private String degreeName;

    @OneToMany(mappedBy = "degree")
    Set<Teacher> teachers;
}
