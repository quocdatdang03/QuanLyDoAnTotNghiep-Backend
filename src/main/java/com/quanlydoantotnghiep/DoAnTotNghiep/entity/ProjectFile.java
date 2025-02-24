package com.quanlydoantotnghiep.DoAnTotNghiep.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ProjectFiles")
public class ProjectFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long projectFileId;

    String nameFile;

    @ManyToOne
    @JoinColumn(name = "projectId", referencedColumnName = "projectId")
    Project project;
}

