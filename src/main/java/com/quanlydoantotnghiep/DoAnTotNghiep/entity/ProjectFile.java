package com.quanlydoantotnghiep.DoAnTotNghiep.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "TAILIEUDETAI")
public class ProjectFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maTaiLieuDeTai")
    Long projectFileId;

    @Column(name = "tenTaiLieu", nullable = false)
    String nameFile;

    @Column(name = "duongDanTaiLieu", nullable = false)
    String pathFile;

    @ManyToOne
    @JoinColumn(name = "maDeTai", referencedColumnName = "maDeTai")
    Project project;
}

