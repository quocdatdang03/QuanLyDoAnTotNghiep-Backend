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
@Table(name = "BAOCAOTIENDO")
public class ProgressReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maBaoCao")
    Long progressReportId;

    @Column(name = "tieuDeBaoCao",columnDefinition = "nvarchar(100)", nullable = false)
    String progressReportName;

    @Column(name = "noiDungBaoCao", columnDefinition = "nvarchar(100)", nullable = false)
    String progressReportContent;

    @Column(name = "trangThaiXetDuyet")
    boolean isApproved;

    @Column(name = "thoiGianGui")
    LocalDateTime createdDate;

    @OneToMany(mappedBy = "progressReport", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ProgressReportFile> progressReportFiles = new ArrayList<>();

    @OneToMany(mappedBy = "progressReport")
    Set<ProgressReview> progressReviews;

    @ManyToOne
    @JoinColumn(name = "maDeTai", referencedColumnName = "maDeTai")
    Project project;

    @ManyToOne
    @JoinColumn(name = "maGiaiDoan", referencedColumnName = "maGiaiDoan")
    Stage stage;

}
