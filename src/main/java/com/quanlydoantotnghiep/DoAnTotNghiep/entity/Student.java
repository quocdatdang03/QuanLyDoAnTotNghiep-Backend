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
@Table(name = "Students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long studentId;

    @OneToOne
    @JoinColumn(name = "accountId", referencedColumnName = "accountId")
    Account account;

    @ManyToOne
    @JoinColumn(name = "classId", referencedColumnName = "classId")
    Clazz clazz;

    @ManyToMany(mappedBy = "students")
    Set<Announcement> announcements = new HashSet<>();

    boolean flagDelete;

}
