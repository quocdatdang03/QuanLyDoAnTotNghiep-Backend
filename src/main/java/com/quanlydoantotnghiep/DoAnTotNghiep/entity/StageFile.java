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
@Table(name = "TAILIEUGIAIDOAN")
public class StageFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maTaiLieuGiaiDoan")
    Long stageFileId;

    @Column(name = "tenTaiLieu", nullable = false)
    String nameFile;

    @Column(name = "duongDanTaiLieu", nullable = false, columnDefinition = "TEXT")
    String pathFile;

    @ManyToOne
    @JoinColumn(name = "maGiaiDoan", referencedColumnName = "maGiaiDoan", nullable = false)
    Stage stage;
}
