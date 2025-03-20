package com.quanlydoantotnghiep.DoAnTotNghiep.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "DETAI_GIAIDOAN")
public class ProjectStage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maDeTaiGiaiDoan")
    Long projectStageId;

    @ManyToOne
    @JoinColumn(name = "maDeTai", referencedColumnName = "maDeTai")
    Project project;

    @ManyToOne
    @JoinColumn(name = "maGiaiDoan", referencedColumnName = "maGiaiDoan")
    Stage stage; // Giai đoạn dùng chung

    @ManyToOne
    @JoinColumn(name = "maTrangThaiGiaiDoan", referencedColumnName = "maTrangThaiGiaiDoan")
    StageStatus stageStatus;  // Trạng thái giai đoạn
}
