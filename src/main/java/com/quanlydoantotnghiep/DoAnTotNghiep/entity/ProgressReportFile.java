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

    @Column(name = "duongDanFile")
    String nameFile;

    @ManyToOne
    @JoinColumn(name = "maBaoCao", referencedColumnName = "maBaoCao")
    ProgressReport progressReport;
}
