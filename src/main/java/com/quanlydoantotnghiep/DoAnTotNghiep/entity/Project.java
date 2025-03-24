package com.quanlydoantotnghiep.DoAnTotNghiep.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
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

    @OneToMany(mappedBy = "project")
    Set<ProgressReport> progressReports;

    @OneToMany(mappedBy = "project")
    Set<ProgressReview> progressReviews;

    // 1 project chỉ thuộc về 1 sinh viên với học kỳ cụ thể
    @OneToOne
    @JoinColumn(name = "maSinhVienHocKy", referencedColumnName = "maSinhVienHocKy")
    StudentSemester studentSemester; // Thay vì trực tiếp gán với Student

    @ManyToOne
    @JoinColumn(name = "maGiangVien", referencedColumnName = "maGiangVien")
    Teacher teacher;

    // 1 project thì có nhiều stage, (quan hệ @ManyToMany được thể hiện qua table ProjectStage)
    @OneToMany(mappedBy = "project")
    List<ProjectStage> projectStages;

    @Column(name = "ngayTao", updatable = false)
    LocalDateTime createdAt;

    @Column(name = "ngayCapNhat")
    LocalDateTime updatedAt;

    @Column(name = "daXoa")
    boolean flagDelete;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
