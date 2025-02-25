package com.quanlydoantotnghiep.DoAnTotNghiep.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "Projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long projectId;

    @Column(columnDefinition = "nvarchar(255)", nullable = false, unique = true)
    String projectName;

    @Column(columnDefinition = "text", nullable = false)
    String projectContent;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ProjectFile> projectFiles = new ArrayList<>();

    @Column(columnDefinition = "text", nullable = false)
    String projectImg;

    @ManyToOne
    @JoinColumn(name = "projectStatusId", referencedColumnName = "projectStatusId")
    ProjectStatus projectStatus;

    @ManyToOne
    @JoinColumn(name = "semesterId", referencedColumnName = "semesterId")
    Semester semester;

    @OneToMany(mappedBy = "project")
    Set<ProgressReport> progressReports;

    @OneToMany(mappedBy = "project")
    Set<ProgressReview> progressReviews;

    @OneToOne
    @JoinColumn(name = "studentId", referencedColumnName = "studentId")
    Student student;

    boolean flagDelete;
}
