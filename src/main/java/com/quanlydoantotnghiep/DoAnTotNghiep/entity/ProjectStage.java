package com.quanlydoantotnghiep.DoAnTotNghiep.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "DETAI_GIAIDOAN",
        uniqueConstraints = @UniqueConstraint(columnNames = {"maDeTai", "maGiaiDoan"})
)
public class ProjectStage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maDeTaiGiaiDoan")
    Long projectStageId;

    @ManyToOne
    @JoinColumn(name = "maDeTai", referencedColumnName = "maDeTai", nullable = false)
    Project project;

    @ManyToOne
    @JoinColumn(name = "maGiaiDoan", referencedColumnName = "maGiaiDoan", nullable = false)
    Stage stage; // Giai đoạn dùng chung

    @ManyToOne
    @JoinColumn(name = "maTrangThaiGiaiDoan", referencedColumnName = "maTrangThaiGiaiDoan", nullable = false)
    StageStatus stageStatus;  // Trạng thái giai đoạn

    // 1 Giai đoạn của 1 đề tài (projectStage) thì sẽ có nhiều progressReport
    @OneToMany(mappedBy = "projectStage", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ProgressReport> progressReports = new ArrayList<>();
}
