package com.quanlydoantotnghiep.DoAnTotNghiep.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "TRANGTHAIGIAIDOAN")
public class StageStatus {

    @Id
    @Column(name = "maTrangThaiGiaiDoan")
    Long stageStatusId;

    @Column(name = "tenTrangThaiGiaiDoan", columnDefinition = "nvarchar(50)", nullable = false)
    String stageStatusName;
}
