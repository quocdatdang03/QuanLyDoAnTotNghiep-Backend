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
    Set<Notification> notifications;

    @OneToMany(mappedBy = "teacher")
    Set<Document> documents;

    @OneToMany(mappedBy = "teacher")
    Set<Stage> stages;

    @OneToMany(mappedBy = "teacher")
    Set<Project> projects;

    // 1 GVHD có thể được đề xuất bởi nhiều sinh viên
    @ManyToMany
    @JoinTable(
            name = "GIANGVIENHD_DEXUAT_SINHVIEN",
            joinColumns = @JoinColumn(name = "maGiangVienHD"),
            inverseJoinColumns = @JoinColumn(name = "maSinhVien")
    )
    Set<Student> students = new HashSet<>();

    @Column(name = "daXoa")
    boolean flagDelete;

  /*

    @OneToOne(mappedBy = "teacher")
    private Clazz clazz;

   */
}
