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

    @Column(name = "duongDanTaiLieuDeTai")
    String nameFile;

    @ManyToOne
    @JoinColumn(name = "maDeTai", referencedColumnName = "maDeTai")
    Project project;
}

