package com.quanlydoantotnghiep.DoAnTotNghiep.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "Announcements")
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long announcementId;

    @Column(columnDefinition = "nvarchar(100)", nullable = false)
    String announcementName;

    @Column(columnDefinition = "text", nullable = false)
    String announcementContent;

    LocalDateTime createdDate;

    @ManyToMany
    @JoinTable(
            name = "Announcement_Student",
            joinColumns = @JoinColumn(name = "announcementId"),
            inverseJoinColumns = @JoinColumn(name = "studentId")
    )
    Set<Student> students = new HashSet<>();

}
