package com.quanlydoantotnghiep.DoAnTotNghiep.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "Documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long documentId;

    @Column(columnDefinition = "text")
    String documentName;

    @Column(columnDefinition = "text")
    String documentDescription;

    @Column(columnDefinition = "text")
    String documentFile;

    boolean flagDelete;

    @ManyToOne
    @JoinColumn(name = "teacherId", referencedColumnName = "teacherId")
    Teacher teacher;
}
