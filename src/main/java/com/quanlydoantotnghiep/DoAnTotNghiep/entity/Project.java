package com.quanlydoantotnghiep.DoAnTotNghiep.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "DETAI")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maDeTai")
    Long projectId;

    @Column(name = "tenDeTai",columnDefinition = "nvarchar(255)", nullable = false, unique = true)
    String projectName;

    @Column(name = "noiDungDeTai", columnDefinition = "text", nullable = false)
    String projectContent;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ProjectFile> projectFiles = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "maTrangThaiDeTai", referencedColumnName = "maTrangThaiDeTai")
    ProjectStatus projectStatus;

    @ManyToOne
    @JoinColumn(name = "maHocKy", referencedColumnName = "maHocKy")
    Semester semester;

    @OneToMany(mappedBy = "project")
    Set<ProgressReport> progressReports;

    @OneToMany(mappedBy = "project")
    Set<ProgressReview> progressReviews;

    @OneToOne
    @JoinColumn(name = "maSinhVien", referencedColumnName = "maSinhVien")
    Student student;

    @ManyToOne
    @JoinColumn(name = "maGiangVienHD", referencedColumnName = "maGiangVienHD")
    Teacher teacher;

    @Column(name = "daXoa")
    boolean flagDelete;
}
