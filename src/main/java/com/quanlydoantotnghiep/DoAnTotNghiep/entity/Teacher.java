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
@Table(name = "GIANGVIENHD")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maGiangVienHD")
    Long teacherId;

    @OneToOne
    @JoinColumn(name = "maTaiKhoan", referencedColumnName = "maTaiKhoan")
    Account account;

    @ManyToOne
    @JoinColumn(name = "maKhoa", referencedColumnName = "maKhoa")
    Faculty faculty;

    @ManyToOne
    @JoinColumn(name = "maHocVi", referencedColumnName = "maHocVi")
    Degree degree;

    @OneToMany(mappedBy = "teacher")
    Set<ProgressReview> progressReviews;


    @OneToMany(mappedBy = "teacher")
    Set<TeacherNotification> teacherNotifications;

    @OneToMany(mappedBy = "teacher")
    Set<Document> documents;

    @OneToMany(mappedBy = "teacher")
    Set<Stage> stages;

    @Column(name = "daXoa")
    boolean flagDelete;

  /*

    @OneToOne(mappedBy = "teacher")
    private Clazz clazz;

   */
}
