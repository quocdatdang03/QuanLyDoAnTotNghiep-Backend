
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
@Table(name = "classes")
public class Clazz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long classId;

    @Column(columnDefinition = "varchar(50)", nullable = false)
    String className;

    @OneToMany(mappedBy = "clazz")
    Set<Student> students;

    @ManyToOne
    @JoinColumn(name = "facultyId", referencedColumnName = "facultyId")
    Faculty faculty;

    boolean flagDelete;
}
