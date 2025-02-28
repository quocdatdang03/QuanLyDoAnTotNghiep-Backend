package com.quanlydoantotnghiep.DoAnTotNghiep.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "TRANGTHAIDETAI")
public class ProjectStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maTrangThaiDeTai")
    Long projectStatusId;

    @Column(name = "tenTrangThaiDeTai")
    String projectStatusName;

    @OneToMany(mappedBy = "projectStatus")
    List<Project> projects;
}
