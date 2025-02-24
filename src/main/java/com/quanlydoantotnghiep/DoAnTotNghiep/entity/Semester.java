//package com.quanlydoantotnghiep.DoAnTotNghiep.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//import lombok.experimental.FieldDefaults;
//
//import java.util.Set;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//@FieldDefaults(level = AccessLevel.PRIVATE)
//@Entity
//@Table(name = "Semesters")
//public class Semester {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    Long semesterId;
//
//    String semesterName;
//
//    @ManyToOne
//    @JoinColumn(name = "schoolYearId", referencedColumnName = "schoolYearId")
//    SchoolYear schoolYear;
//
//    @ManyToMany
//    @JoinTable(
//            name = "Semester_Account",
//            joinColumns = @JoinColumn(name = "semesterId"),
//            inverseJoinColumns = @JoinColumn(name = "accountId")
//    )
//    Set<Account> accounts;
//}
