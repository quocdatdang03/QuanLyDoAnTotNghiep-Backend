package com.quanlydoantotnghiep.DoAnTotNghiep.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "GIAIDOAN")
public class Stage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maGiaiDoan")
    Long stageId;

    @Column(name = "tenGiaiDoan", columnDefinition = "nvarchar(50)", nullable = false)
    String stageName;

    @Column(name = "tieuDeGiaiDoan", nullable = false)
    String stageTitle;

    @Column(name = "noiDungGiaiDoan", columnDefinition = "text", nullable = false)
    String stageContent;

    @Column(name = "thuTuGiaiDoan", nullable = false)
    Integer stageOrder;

    @Column(name = "thoiGianBatDau")
    LocalDateTime startDate;

    @Column(name = "thoiGianKetThuc")
    LocalDateTime endDate;

    // 1 Stage có thể có trong nhiều project, (quan hệ @ManyToMany được thể hiện qua table ProjectStage)
    @OneToMany(mappedBy = "stage", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ProjectStage> projectStages;

    // Đây là default stageStatus chung cho các project
    @ManyToOne
    @JoinColumn(name = "maTrangThaiGiaiDoan", referencedColumnName = "maTrangThaiGiaiDoan")
    StageStatus stageStatus;

    @ManyToOne
    @JoinColumn(name = "maGiangVienHD", referencedColumnName = "maGiangVienHD")
    Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "maHocKy", referencedColumnName = "maHocKy")
    Semester semester;

    @Column(name = "daXoa")
    boolean flagDelete;


    // 1 Stage có nhiều Stage File
    @OneToMany(mappedBy = "stage", cascade = CascadeType.ALL, orphanRemoval = true)
    List<StageFile> stageFiles = new ArrayList<>();
}
