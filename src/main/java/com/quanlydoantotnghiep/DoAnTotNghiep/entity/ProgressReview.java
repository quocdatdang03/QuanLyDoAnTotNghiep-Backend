package com.quanlydoantotnghiep.DoAnTotNghiep.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "ProgressReviews")
public class ProgressReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long progressReviewId;

    @Column(columnDefinition = "nvarchar(255)",nullable = false)
    String progressReviewTitle;

    @Column(columnDefinition = "text",nullable = false)
    String progressReviewContent;

    boolean isApproved;

    LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name="teacherId", referencedColumnName = "teacherId")
    Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "project", referencedColumnName = "projectId")
    Project project;

    @ManyToOne
    @JoinColumn(name = "progressReportId", referencedColumnName = "progressReportId")
    ProgressReport progressReport;
}
