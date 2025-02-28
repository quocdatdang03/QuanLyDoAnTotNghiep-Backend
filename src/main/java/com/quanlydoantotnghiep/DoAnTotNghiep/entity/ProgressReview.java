package com.quanlydoantotnghiep.DoAnTotNghiep.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "DANHGIATIENDO")
public class ProgressReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maDanhGia")
    Long progressReviewId;

    @Column(name = "tieuDeDanhGia", columnDefinition = "nvarchar(255)",nullable = false)
    String progressReviewTitle;

    @Column(name = "noiDungDanhGia", columnDefinition = "text",nullable = false)
    String progressReviewContent;

    @Column(name = "trangThaiDanhGia")
    boolean isApproved;

    @Column(name = "ngayGui")
    LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name="maGiangVienHD", referencedColumnName = "maGiangVienHD")
    Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "maDeTai", referencedColumnName = "maDeTai")
    Project project;

    @ManyToOne
    @JoinColumn(name = "maBaoCao", referencedColumnName = "maBaoCao")
    ProgressReport progressReport;
}
