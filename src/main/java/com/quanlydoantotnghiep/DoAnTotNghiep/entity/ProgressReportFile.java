package com.quanlydoantotnghiep.DoAnTotNghiep.entity;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "TAILIEUBAOCAO")
public class ProgressReportFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maTaiLieuBaoCao")
    Long progressReportFileId;

    @Column(name = "tenTaiLieu", nullable = false)
    String nameFile;

    @Column(name = "duongDanTaiLieu", nullable = false)
    String pathFile;

    @ManyToOne
    @JoinColumn(name = "maBaoCao", referencedColumnName = "maBaoCao", nullable = false)
    ProgressReport progressReport;
}
