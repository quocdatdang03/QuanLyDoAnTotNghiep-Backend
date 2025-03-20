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
@Table(name = "TAILIEUDANHGIA")
public class ProgressReviewFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maTaiLieuDanhGia")
    Long progressReviewFileId;

    @Column(name = "tenTaiLieu", nullable = false)
    String nameFile;

    @Column(name = "duongDanTaiLieu", nullable = false)
    String pathFile;

    @ManyToOne
    @JoinColumn(name = "maDanhGia", referencedColumnName = "maDanhGia")
    ProgressReview progressReview;
}
