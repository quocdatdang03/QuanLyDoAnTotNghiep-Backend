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
@Builder
@Entity
@Table(name = "Teachers")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long teacherId;

    @OneToOne
    @JoinColumn(name = "accountId", referencedColumnName = "accountId")
    Account account;

    @ManyToOne
    @JoinColumn(name = "facultyId", referencedColumnName = "facultyId")
    Faculty faculty;

    @ManyToOne
    @JoinColumn(name = "degreeId", referencedColumnName = "degreeId")
    Degree degree;

    @OneToMany(mappedBy = "teacher")
    Set<ProgressReview> progressReviews;


    @OneToMany(mappedBy = "teacher")
    Set<TeacherNotification> teacherNotifications;

    @OneToMany(mappedBy = "teacher")
    Set<Document> documents;

    @OneToMany(mappedBy = "teacher")
    Set<Stage> stages;

    boolean flagDelete;

  /*

    @OneToOne(mappedBy = "teacher")
    private Clazz clazz;

   */
}
