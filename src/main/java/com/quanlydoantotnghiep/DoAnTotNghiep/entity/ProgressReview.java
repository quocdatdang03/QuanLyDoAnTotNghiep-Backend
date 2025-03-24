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

    @ManyToOne
    @JoinColumn(name="maGiangVien", referencedColumnName = "maGiangVien")
    Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "maDeTai", referencedColumnName = "maDeTai")
    Project project;

    @ManyToOne
    @JoinColumn(name = "maBaoCao", referencedColumnName = "maBaoCao")
    ProgressReport progressReport;

    @OneToMany(mappedBy = "progressReview", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ProgressReviewFile> progressReviewFiles = new ArrayList<>();

    @Column(name = "ngayTao")
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
