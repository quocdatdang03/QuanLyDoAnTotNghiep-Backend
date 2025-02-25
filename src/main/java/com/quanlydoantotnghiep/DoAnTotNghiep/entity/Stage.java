package com.quanlydoantotnghiep.DoAnTotNghiep.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "Stages")
public class Stage { // GIAI ĐOẠN

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long stageId;

    @Column(columnDefinition = "nvarchar(50)", nullable = false)
    String stageName;

    LocalDateTime startDate;

    LocalDateTime endDate;

    @OneToMany(mappedBy = "stage")
    Set<ProgressReport> progressReports;

    @ManyToOne
    @JoinColumn(name = "teacherId", referencedColumnName = "teacherId")
    Teacher teacher;
}
