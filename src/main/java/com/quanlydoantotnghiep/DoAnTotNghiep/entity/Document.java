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
@Table(name = "TAILIEU")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maTaiLieu")
    Long documentId;

    @Column(name = "tenTaiLieu",columnDefinition = "text")
    String documentName;

    @Column(name = "moTa",columnDefinition = "text")
    String documentDescription;

    @Column(name = "duongDanFile",columnDefinition = "text")
    String documentFile;

    @Column(name = "daXoa")
    boolean flagDelete;

    @ManyToOne
    @JoinColumn(name = "maGiangVienHD", referencedColumnName = "maGiangVienHD")
    Teacher teacher;
}
