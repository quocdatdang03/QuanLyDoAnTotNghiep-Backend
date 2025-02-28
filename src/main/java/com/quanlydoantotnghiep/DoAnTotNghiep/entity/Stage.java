package com.quanlydoantotnghiep.DoAnTotNghiep.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "GIAIDOAN")
public class Stage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maGiaiDoan")
    Long stageId;

    @Column(name = "tenGiaiDoan", columnDefinition = "nvarchar(50)", nullable = false)
    String stageName;

    @Column(name = "thoiGianBatDau")
    LocalDateTime startDate;

    @Column(name = "thoiGianKetThuc")
    LocalDateTime endDate;

    @OneToMany(mappedBy = "stage")
    Set<ProgressReport> progressReports;

    @ManyToOne
    @JoinColumn(name = "maGiangVienHD", referencedColumnName = "maGiangVienHD")
    Teacher teacher;
}
