package com.quanlydoantotnghiep.DoAnTotNghiep.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "DEXUAT_SINHVIEN_GIANGVIEN")
public class StudentTeacherProposal {

    @EmbeddedId
    StudentTeacherProposalId id;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "maSinhVien", referencedColumnName = "maSinhVien", nullable = false)
    Student student;

    @ManyToOne
    @MapsId("teacherId")
    @JoinColumn(name = "maGiangVien", referencedColumnName = "maGiangVien", nullable = false)
    Teacher teacher;

    @ManyToOne
    @MapsId("semesterId")
    @JoinColumn(name = "maHocKy", referencedColumnName = "maHocKy", nullable = false)
    Semester semester;

    @Column(name = "daXoa", nullable = false)
    boolean flagDelete;
}
