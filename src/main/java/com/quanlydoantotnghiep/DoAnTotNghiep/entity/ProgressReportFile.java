package com.quanlydoantotnghiep.DoAnTotNghiep.entity;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ProgressReportFiles")
public class ProgressReportFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long progressReportFileId;

    String nameFile;

    @ManyToOne
    @JoinColumn(name = "progressReportId", referencedColumnName = "progressReportId")
    ProgressReport progressReport;
}
