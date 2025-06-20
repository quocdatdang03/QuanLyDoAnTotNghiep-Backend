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
    @JoinColumn(name="maGiangVien", referencedColumnName = "maGiangVien", nullable = false)
    Teacher teacher;

    // 1 progressReview thì chỉ thuộc về 1 progressReport
    @ManyToOne
    @JoinColumn(name = "maBaoCao", referencedColumnName = "maBaoCao", nullable = false)
    ProgressReport progressReport;

    @OneToMany(mappedBy = "progressReview", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ProgressReviewFile> progressReviewFiles = new ArrayList<>();

    @Column(name = "ngayTao")
    LocalDateTime createdAt;

    @Column(name = "ngayCapNhat")
    LocalDateTime updatedAt;

    @Column(name = "trangThaiXoa")
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
