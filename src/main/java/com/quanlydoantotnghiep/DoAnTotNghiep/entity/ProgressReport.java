package com.quanlydoantotnghiep.DoAnTotNghiep.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
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
@Table(name = "ProgressReports")
public class ProgressReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long progressReportId;

    @Column(columnDefinition = "nvarchar(100)", nullable = false)
    String progressReportName;

    @Column(columnDefinition = "nvarchar(100)", nullable = false)
    String progressReportContent;

    boolean isApproved;

    LocalDateTime createdDate;

    @OneToMany(mappedBy = "progressReport", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ProgressReportFile> progressReportFiles = new ArrayList<>();

    @OneToMany(mappedBy = "progressReport")
    Set<ProgressReview> progressReviews;

    @ManyToOne
    @JoinColumn(name = "projectId", referencedColumnName = "projectId")
    Project project;

    @ManyToOne
    @JoinColumn(name = "stageId", referencedColumnName = "stageId")
    Stage stage;

}
