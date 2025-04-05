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
@Table(name = "GIANGVIEN")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maGiangVien")
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

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Notification> notifications = new HashSet<>();

//    @OneToMany(mappedBy = "teacher")
//    Set<Document> documents;

    @OneToMany(mappedBy = "teacher")
    Set<Stage> stages;

    @OneToMany(mappedBy = "teacher")
    Set<Project> projects;

    // 1 GVHD có thể được đề xuất bởi nhiều sinh viên theo tung hoc ky
    @OneToMany(mappedBy = "teacher")
    Set<StudentTeacherProposal> proposedByStudents = new HashSet<>();

    // 1 GVHD có thể quản lý nhiều sinh viên ở nhiều học kỳ
    @OneToMany(mappedBy = "instructor")
    Set<StudentSemester> studentSemesters = new HashSet<>();

    @Column(name = "truongBoMon")
    boolean isLeader;

    @Column(name = "daXoa")
    boolean flagDelete;

  /*

    @OneToOne(mappedBy = "teacher")
    private Clazz clazz;

   */
}
